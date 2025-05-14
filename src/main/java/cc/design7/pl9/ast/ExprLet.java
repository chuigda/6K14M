package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

import java.util.List;

public record ExprLet(
        SourceLocation location,
        boolean rec,
        List<String> vars,
        List<IExpr> values,
        IExpr body,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprLet(
            SourceLocation location,
            boolean rec,
            List<String> vars,
            List<IExpr> values,
            IExpr body
    ) {
        this(location, rec, vars, values, body, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
