package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;

public record ExprContinue(SourceLocation location) implements IExpr {
    @Override
    public Type type() {
        throw new UnsupportedOperationException("type() not supported for ExprContinue");
    }
}
