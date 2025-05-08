package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

import java.util.List;

public record ExprApp(
        SourceLocation location,
        IExpr fn,
        List<IExpr> args,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprApp(SourceLocation location, IExpr fn, List<IExpr> args) {
        this(location, fn, args, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
