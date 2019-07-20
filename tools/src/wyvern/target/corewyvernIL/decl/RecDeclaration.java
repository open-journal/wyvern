package wyvern.target.corewyvernIL.decl;

import java.util.Set;

import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.decltype.DeclType;
//import wyvern.target.corewyvernIL.support.TopLevelContext;
import wyvern.target.corewyvernIL.expression.IExpr;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.typedAST.interfaces.ExpressionAST;

public class RecDeclaration extends DeclarationWithRHS {
  private ExpressionAST body;
  private ValueType type;

  public     RecDeclaration(String fieldName, ValueType type, IExpr iExpr, FileLocation loc) {
    super(fieldName, iExpr, loc);
    this.type = type;
  }

  @Override
  public <S, T> T acceptVisitor(ASTVisitor<S, T> visitor, S state) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DeclType getDeclType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ValueType getType() {
    return null;
  }

  @Override
  public Set<String> getFreeVariables() {
    // TODO Auto-generated method stub
    return null;
  }


}
