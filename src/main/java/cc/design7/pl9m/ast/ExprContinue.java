package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

public record ExprContinue(
        SourceLocation location,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprContinue(SourceLocation location) {
        this(location, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
