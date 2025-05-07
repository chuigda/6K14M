package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;

import java.util.List;

public record ExprApp(
        SourceLocation location,
        IExpr fn,
        List<IExpr> args,
        Ref<Type> typeRef
) implements IExpr, ITypeResolvable {
    public ExprApp(SourceLocation location, IExpr fn, List<IExpr> args) {
        this(location, fn, args, new Ref<>());
    }

    @Override
    public Type type() {
        return typeRef.value;
    }
}
