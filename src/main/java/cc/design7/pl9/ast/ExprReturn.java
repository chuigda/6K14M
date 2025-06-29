package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;
import org.jetbrains.annotations.Nullable;

public record ExprReturn(
        SourceLocation location,
        @Nullable IExpr value,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprReturn(SourceLocation location, @Nullable IExpr value) {
        this(location, value, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
