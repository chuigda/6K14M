package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

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
