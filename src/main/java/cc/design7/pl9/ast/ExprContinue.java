package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

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
