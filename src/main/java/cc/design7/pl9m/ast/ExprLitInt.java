package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.tyck.TypeOp;
import org.jetbrains.annotations.NotNull;

public record ExprLitInt(long value) implements Expr {
    @Override
    public Type type() {
        return TypeOp.INT_TYPE;
    }

    @Override
    public @NotNull String toString() {
        return Long.toString(value);
    }
}
