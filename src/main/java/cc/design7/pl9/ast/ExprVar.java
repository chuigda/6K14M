package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

public record ExprVar(
        SourceLocation location,
        String varName,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprVar(SourceLocation location, String varName) {
        this(location, varName, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
