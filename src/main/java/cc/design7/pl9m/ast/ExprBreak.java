package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.Type;
import org.jetbrains.annotations.Nullable;

public record ExprBreak(SourceLocation location, @Nullable IExpr value) implements IExpr {
    @Override
    public Type type() {
        throw new UnsupportedOperationException("type() not supported for ExprBreak");
    }
}
