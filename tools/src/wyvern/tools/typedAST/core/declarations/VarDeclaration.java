package wyvern.tools.typedAST.core.declarations;



import java.util.LinkedList;
import java.util.List;

import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.VarDeclType;
import wyvern.target.corewyvernIL.expression.MethodCall;
import wyvern.target.corewyvernIL.expression.Variable;
import wyvern.target.corewyvernIL.modules.TypedModuleSpec;
import wyvern.target.corewyvernIL.support.GenContext;
import wyvern.target.corewyvernIL.support.TopLevelContext;
import wyvern.target.corewyvernIL.type.StructuralType;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.typedAST.abs.Declaration;
import wyvern.tools.typedAST.core.binding.NameBindingImpl;
import wyvern.tools.typedAST.core.expressions.New;
import wyvern.tools.typedAST.interfaces.CoreAST;
import wyvern.tools.typedAST.interfaces.ExpressionAST;
import wyvern.tools.typedAST.interfaces.TypedAST;
import wyvern.tools.types.Type;
import wyvern.tools.util.GetterAndSetterGeneration;

public class VarDeclaration extends Declaration implements CoreAST {
	ExpressionAST definition;
	Type definitionType;
	String name;
	//NameBinding binding;

	private boolean isClass;
	public boolean isClassMember() {
		return isClass;
	}

	public VarDeclaration(String varName, Type parsedType, TypedAST definition, FileLocation loc) {
		this.definition=(ExpressionAST)definition;
		definitionType = parsedType;
		name = varName;
		//binding = new AssignableNameBinding(varName, parsedType);
		this.location = loc;
	}

	@Override
	public String getName() {
		return name;
	}
	
    @Override
    public Type getType() {
        return definitionType;
    }
    
	public TypedAST getDefinition() {
		return definition;
	}

	private FileLocation location = FileLocation.UNKNOWN;
	public FileLocation getLocation() {
		return this.location;
	}

	@Override
	public DeclType genILType(GenContext ctx) {
		ValueType vt = definitionType.getILType(ctx);
		return new VarDeclType(getName(), vt);
	}

	@Override
	public wyvern.target.corewyvernIL.decl.Declaration generateDecl(GenContext ctx, GenContext thisContext) {
		
		// Create a var declaration. Getters and setters for the var are generated by the enclosing instance of New.
		// TODO: ideally want the getters and setters to be generated here?
		wyvern.target.corewyvernIL.decl.VarDeclaration varDecl;
		varDecl = new wyvern.target.corewyvernIL.decl.VarDeclaration(getName(), definitionType.getILType(ctx), definition.generateIL(ctx, null, null), location);
		return varDecl;

	}

	@Override
	public wyvern.target.corewyvernIL.decl.Declaration topLevelGen(GenContext ctx, List<TypedModuleSpec> dependencies) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Get the ValueType of this VarDeclaration. Sets the definitionType if it hasn't been already.
	 * @param ctx: context to evaluate in.
	 * @return ValueType of this VarDeclaration.
	 */
	private ValueType getILValueType (GenContext ctx) {
		return definitionType.getILType(ctx);
	}
	
	@Override
	public void genTopLevel (TopLevelContext tlc) {
		
		GenContext ctx = tlc.getContext();
		
		// Figure out name and type of this variable.
		String varName = this.getName();
		Type varType = this.getType();
		ValueType varValueType = getILValueType(ctx);
		
		// Create a temp object with a single var declaration.
		VarDeclaration varDecl = new VarDeclaration(varName, definitionType, this.definition, location);
		DeclSequence tempObjBody = new DeclSequence(varDecl);
		New tempObj = new New(tempObjBody, location);
		String tempObjName = GetterAndSetterGeneration.varNameToTempObj(varName);
		ValDeclaration letDecl = new ValDeclaration(tempObjName, tempObj, null);
		
		// Update context.
		letDecl.genTopLevel(tlc);
		ctx = tlc.getContext();
		
		// Create variables for the temp object to be used for the getter and setter.
		wyvern.tools.typedAST.core.expressions.Variable tempObjForSetter, tempObjForGetter;
		tempObjForGetter = new wyvern.tools.typedAST.core.expressions.Variable(new NameBindingImpl(tempObjName, null), null);
		tempObjForSetter = new wyvern.tools.typedAST.core.expressions.Variable(new NameBindingImpl(tempObjName, null), null);
		
		// Create getter and setter.
		DefDeclaration getter, setter;
		getter = DefDeclaration.generateGetter(ctx, tempObjForGetter, varName, varType);
		setter = DefDeclaration.generateSetter(ctx, tempObjForSetter, varName, varType);
		
		// Figure out structural type from declared types.
		List<DeclType> declarationTypes = new LinkedList<>();
		declarationTypes.add(getter.genILType(ctx));
		declarationTypes.add(setter.genILType(ctx));
		String newName = GenContext.generateName();
		// If it is a var declaration, it must be of resource type
		StructuralType structType = new StructuralType(newName, declarationTypes, true);
		ctx = ctx.extend(newName, new Variable(newName), structType);
		tlc.updateContext(ctx);
		
		// Group getter and setter into a single declaration block.
		List<wyvern.target.corewyvernIL.decl.Declaration> declarations = new LinkedList<>();
		wyvern.target.corewyvernIL.decl.Declaration getterIL, setterIL;
		getterIL = getter.generateDecl(ctx, ctx);
		setterIL = setter.generateDecl(ctx, ctx);
		declarations.add(getterIL);
		declarations.add(setterIL);
		
		// Wrap declarations with a New expression and add to top-level.
		wyvern.target.corewyvernIL.expression.New newExp;
		newExp = new wyvern.target.corewyvernIL.expression.New(declarations, newName, structType, getLocation());
		tlc.addLet(newName, structType, newExp, true);
		
		// Equate the var with a method call on its getter.
		// This means top-lever var reads are actually calls to the getter method.
		MethodCall methodCallExpr = new MethodCall(new Variable(newName), getter.getName(), new LinkedList<>(), this);
		ctx = ctx.extend(varName, methodCallExpr, varValueType);
		tlc.updateContext(ctx);
	}

	@Override
	public void addModuleDecl(TopLevelContext tlc) {
		// do nothing--adding module declarations handled by genTopLevel method above.
		// overriding this is needed as the default throws an exception.
		return;
	}
	
}
