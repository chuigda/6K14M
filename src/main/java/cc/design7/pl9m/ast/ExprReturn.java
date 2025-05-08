package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;
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
