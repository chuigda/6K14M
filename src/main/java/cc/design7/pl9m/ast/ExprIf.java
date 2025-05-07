package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;
import org.jetbrains.annotations.Nullable;

public record ExprIf(
        SourceLocation location,
        IExpr cond,
        IExpr then,
        @Nullable IExpr otherwise,
        Ref<Type> typeRef
) implements IExpr, ITypeResolvable {
    public ExprIf(SourceLocation location, IExpr cond, IExpr then, @Nullable IExpr otherwise) {
        this(location, cond, then, otherwise, new Ref<>());
    }

    @Override
    public Type type() {
        return typeRef.value;
    }
}
