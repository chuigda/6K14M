package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;
import org.jetbrains.annotations.Nullable;

public record ExprIf(
        SourceLocation location,
        IExpr cond,
        IExpr then,
        @Nullable IExpr otherwise,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprIf(SourceLocation location, IExpr cond, IExpr then, @Nullable IExpr otherwise) {
        this(location, cond, then, otherwise, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
