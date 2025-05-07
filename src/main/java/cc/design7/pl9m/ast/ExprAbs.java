package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;

import java.util.List;

public record ExprAbs(
        SourceLocation location,
        List<String> args,
        IExpr body,
        Ref<Type> typeRef
) implements IExpr, ITypeResolvable {
    public ExprAbs(SourceLocation location, List<String> args, IExpr body) {
        this(location, args, body, new Ref<>());
    }

    @Override
    public Type type() {
        return typeRef().value;
    }
}
