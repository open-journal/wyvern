package wyvern.tools.typedAST.core.declarations;

import java.util.List;
import wyvern.target.corewyvernIL.BindingSite;
import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.ValDeclType;
import wyvern.target.corewyvernIL.modules.TypedModuleSpec;
import wyvern.target.corewyvernIL.support.GenContext;
import wyvern.target.corewyvernIL.support.TopLevelContext;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.ErrorMessage;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.errors.ToolError;
import wyvern.tools.typedAST.abs.Declaration;
import wyvern.tools.typedAST.core.binding.NameBinding;
import wyvern.tools.typedAST.core.binding.NameBindingImpl;
import wyvern.tools.typedAST.interfaces.CoreAST;
import wyvern.tools.typedAST.interfaces.ExpressionAST;
import wyvern.tools.typedAST.interfaces.TypedAST;
import wyvern.tools.typedAST.typedastvisitor.TypedASTVisitor;
import wyvern.tools.types.Type;

public class RecConstructDeclaration extends Declaration implements CoreAST {
  private String variableName;
  private Type declaredType;
  private ExpressionAST body;
  private NameBinding binding;
  private FileLocation location;

  public RecConstructDeclaration(String variableName, Type declaredType, TypedAST body, FileLocation location) {
    this.variableName = variableName;
    this.declaredType = declaredType;
    this.body = (ExpressionAST) body;
    this.binding = new NameBindingImpl(variableName, declaredType);
    this.location = location;
  }

  @Override
  public Type getType() {
    return this.declaredType;
  }

  @Override
  public String getName() {
    return this.variableName;
  }

  @Override
  public FileLocation getLocation() {
    return this.location;
  }

  public ExpressionAST getBody() {
    return this.body;
  }

  @Override
  public <S, T> T acceptVisitor(TypedASTVisitor<S, T> visitor, S state) {
    System.out.println("visited recConstruct");
    return visitor.visit(state, this);
  }

  @Override
  public void genTopLevel(TopLevelContext tlc) {
    ValueType declType = getILValueType(tlc.getContext());
    tlc.addLet(new BindingSite(getName()), getILValueType(tlc.getContext()),
        this.body.generateIL(tlc.getContext(), declType, tlc.getDependencies()), false);
  }

  @Override
  public DeclType genILType(GenContext ctx) {
    ValueType vt = getILValueType(ctx);
    return new ValDeclType(getName(), vt);
  }

  private ValueType getILValueType(GenContext ctx) {
    ValueType vt;
    if (declaredType != null) {
      vt = declaredType.getILType(ctx);
    } else {
      final Type type = this.binding.getType();
      if (type != null) {
        vt = type.getILType(ctx);
      } else {
        vt = this.body.generateIL(ctx, null, null).typeCheck(ctx, null);
      }
    }
    return vt;
  }

  @Override
  public wyvern.target.corewyvernIL.decl.Declaration generateDecl(GenContext ctx, GenContext thisContext) {

    ValueType expectedType = getILValueType(thisContext);
    return new wyvern.target.corewyvernIL.decl.RecConstructDeclaration(getName(), expectedType,
        this.body.generateIL(ctx, expectedType, null), location);
  }

  @Override
  public void addModuleDecl(TopLevelContext tlc) {
    wyvern.target.corewyvernIL.decl.Declaration decl = new wyvern.target.corewyvernIL.decl.RecConstructDeclaration(
        this.getName(), getILValueType(tlc.getContext()),
        new wyvern.target.corewyvernIL.expression.Variable(this.getName()), location);
    DeclType dt = genILType(tlc.getContext());
    tlc.addModuleDecl(decl, dt);
  }

  @Override
  public StringBuilder prettyPrint() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n    ");
    sb.append(variableName);
    sb.append(" : ");
    sb.append(declaredType);
    sb.append(" = ");
    sb.append(this.body.prettyPrint());
    return sb;
  }

  @Override
  public wyvern.target.corewyvernIL.decl.Declaration topLevelGen(GenContext ctx, List<TypedModuleSpec> dependencies) {
    return null;
  }
}
