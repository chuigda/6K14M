package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

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
