package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;

public record ExprVar(
        SourceLocation location,
        String varName,
        Ref<Type> typeRef
) implements IExpr, ITypeResolvable {
    public ExprVar(SourceLocation location, String varName) {
        this(location, varName, new Ref<>());
    }

    @Override
    public Type type() {
        return typeRef.value;
    }
}
