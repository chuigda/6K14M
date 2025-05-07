package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.tyck.TypeOp;
import org.jetbrains.annotations.NotNull;

public record ExprLitFloat(double value) implements Expr {
    @Override
    public Type type() {
        return TypeOp.FLOAT_TYPE;
    }

    @Override
    public @NotNull String toString() {
        return Double.toString(value);
    }
}
