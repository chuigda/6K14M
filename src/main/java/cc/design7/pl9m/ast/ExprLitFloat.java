package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.tyck.TypeOp;

public record ExprLitFloat(SourceLocation location, double value) implements IExpr {
    @Override
    public Type type() {
        return TypeOp.FLOAT_TYPE;
    }
}
