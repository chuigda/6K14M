package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

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
