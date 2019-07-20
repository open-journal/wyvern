package wyvern.target.corewyvernIL.decltype;

import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.support.FailureReason;
import wyvern.target.corewyvernIL.support.TypeContext;
import wyvern.target.corewyvernIL.support.View;
import wyvern.target.corewyvernIL.type.ValueType;

public class RecDeclType extends DeclTypeWithResult {
    public RecDeclType(String name, ValueType valueType) {
        super(name, valueType);
    }

    @Override
    public boolean isSubtypeOf(DeclType dt, TypeContext ctx, FailureReason reason) {
        return false;
    }

    @Override
    public DeclType adapt(View v) {
        return null;
    }

    @Override
    public DeclType doAvoid(String varName, TypeContext ctx, int count) {
        return null;
    }

    @Override
    public boolean isTypeOrEffectDecl() {
        return false;
    }

    @Override
    public <S, T> T acceptVisitor(ASTVisitor<S, T> visitor, S state) {
        return null;
    }
}
