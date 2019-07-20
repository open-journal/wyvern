package wyvern.target.corewyvernIL.expression;

import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.effects.EffectAccumulator;
import wyvern.target.corewyvernIL.support.EvalContext;
import wyvern.target.corewyvernIL.support.TypeContext;
import wyvern.target.corewyvernIL.type.ValueType;

import java.util.Set;

public class PlaceHolderValue extends Expression {
    @Override
    public ValueType typeCheck(TypeContext ctx, EffectAccumulator effectAccumulator) {
        return null;
    }

    @Override
    public Value interpret(EvalContext ctx) {
        return null;
    }

    @Override
    public Set<String> getFreeVariables() {
        return null;
    }

    @Override
    public <S, T> T acceptVisitor(ASTVisitor<S, T> visitor, S state) {
        return null;
    }
}
