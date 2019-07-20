package wyvern.target.corewyvernIL.expression;
import wyvern.target.corewyvernIL.BindingSite;
import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.effects.EffectAccumulator;
import wyvern.target.corewyvernIL.support.EvalContext;
import wyvern.target.corewyvernIL.support.TypeContext;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.FileLocation;

import java.util.List;
import java.util.Set;

public class RecExpression extends Expression implements Invokable {
    public RecExpression(BindingSite site, ValueType valueType) {

    }
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

    @Override
    public Value invoke(String methodName, List<Value> args, FileLocation loc) {
        return null;
    }

    @Override
    public Value getField(String fieldName) {
        return null;
    }
}
