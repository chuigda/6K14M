package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

public record ExprLoop(
        SourceLocation location,
        IExpr body,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprLoop(SourceLocation location, IExpr body) {
        this(location, body, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
