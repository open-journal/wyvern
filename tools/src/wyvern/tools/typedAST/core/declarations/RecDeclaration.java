package wyvern.tools.typedAST.core.declarations;

import java.util.LinkedList;
import java.util.List;

import wyvern.target.corewyvernIL.BindingSite;
import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.RecDeclType;
import wyvern.target.corewyvernIL.expression.Variable;
import wyvern.target.corewyvernIL.modules.TypedModuleSpec;
import wyvern.target.corewyvernIL.support.GenContext;
import wyvern.target.corewyvernIL.support.TopLevelContext;
import wyvern.target.corewyvernIL.type.StructuralType;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.typedAST.interfaces.ExpressionAST;
import wyvern.tools.types.Type;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.typedAST.abs.Declaration;
import wyvern.tools.typedAST.core.Sequence;
import wyvern.tools.typedAST.interfaces.CoreAST;
import wyvern.tools.typedAST.interfaces.TypedAST;
import wyvern.tools.typedAST.typedastvisitor.TypedASTVisitor;

public class RecDeclaration extends Declaration implements CoreAST {
    private List<TypedAST> body;
    private ExpressionAST originalBody;
    private String variableName; // generated fresh variable
    private FileLocation location = FileLocation.UNKNOWN;
    private BindingSite bindingSite;
   // private List<wyvern.target.corewyvernIL.decl.Declaration> declarations;

    public RecDeclaration(TypedAST body, FileLocation location) {
        this.originalBody = (ExpressionAST) body;
        this.body = ((Sequence) body).getExps();
        this.location = location;
        this.variableName = GenContext.generateName(); // generate fresh variable
        this.bindingSite = new BindingSite(this.variableName);
        System.out.println();
        System.out.println("RecDeclaration Body: " + this.body); // debugger
        System.out.println();
        System.out.println("RecDeclaration Variable: " + this.variableName);
    }

    @Override
    public <S, T> T acceptVisitor(TypedASTVisitor<S, T> visitor, S state) {
        // TODO Auto-generated method stub
        return visitor.visit(state, this);
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.variableName;
    }

    @Override
    public DeclType genILType(GenContext ctx) {
        ctx = this.extendContext(ctx);
        RecDeclType returnObject = new RecDeclType(this.getName(), getILValueType(ctx) /* to be debugger*/); //debugger

        return returnObject;
    }

    @Override
    public wyvern.target.corewyvernIL.decl.Declaration generateDecl(GenContext ctx, GenContext thisContext) {
        ValueType expectedType = this.getILValueType(thisContext);
        /* uses ctx for generating the definition, as the selfName is not in scope */
        return new wyvern.target.corewyvernIL.decl.RecDeclaration(this.getName(), expectedType, 
            this.originalBody.generateIL(ctx, expectedType, null), location);
    }


    @Override
    public wyvern.target.corewyvernIL.decl.Declaration topLevelGen(GenContext ctx, List<TypedModuleSpec> dependencies) {
        return generateDecl(ctx, ctx);
    }

    public BindingSite getSite() {
        return this.bindingSite;
    }

    @Override
    public void genTopLevel(TopLevelContext tlc) {
        GenContext ctx = tlc.getContext();
        ValueType declType = getILValueType(ctx);

        // Wrap declarations with a New expression and add to top-level.
        //wyvern.target.corewyvernIL.expression.New newExp;
        //newExp = new wyvern.target.corewyvernIL.expression.New(this.declarations, this.getSite(), declType, getLocation());

        // debugger
        
        tlc.addLet(this.getSite(),
                getILValueType(tlc.getContext()),
                //newExp,
                this.originalBody.generateIL(tlc.getContext(), declType, tlc.getDependencies()),
                true);

        //tlc.updateContext(this.extendContext(ctx));
        //System.out.println("TLC: " + tlc);
    }

    @Override
    public FileLocation getLocation() {
        return this.location;
    }

    @Override // debug
    public void addModuleDecl(TopLevelContext tlc) {
        wyvern.target.corewyvernIL.decl.Declaration decl =
                new wyvern.target.corewyvernIL.decl.RecDeclaration(this.getName(), getILValueType(tlc.getContext()),
                        new wyvern.target.corewyvernIL.expression.Variable(this.getName()), location);
        DeclType dt = genILType(tlc.getContext());
        tlc.addModuleDecl(decl, dt);
    }

    private GenContext extendContext(GenContext ctx) {
        String constructVariableName;
        Type type;
        ValueType valueType;
        for (TypedAST arg : this.body) {
            constructVariableName = ((RecConstructDeclaration) arg).getName();
            type = ((RecConstructDeclaration) arg).getType();
            valueType = type.getILType(ctx);
            ctx = ctx.extend(constructVariableName,
                    new Variable(this.getSite(), this.getLocation()), /*replace variable with PlaceHolderType in the future*/
                    valueType);
        }
        return ctx;
    }

    private ValueType getILValueType(GenContext ctx) {
        List<DeclType> typeList = new LinkedList<DeclType>();

        for (TypedAST arg : this.body) {
            typeList.add(((RecConstructDeclaration) arg).genILType(ctx));
            //this.declarations.add((wyvern.target.corewyvernIL.decl.RecConstructDeclaration) arg);
        }

        StructuralType valueType = new StructuralType(this.getSite(), typeList, false, this.getLocation());
        return valueType;
    }
}
