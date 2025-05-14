package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

import java.util.List;

public record ExprAbs(
        SourceLocation location,
        List<String> params,
        IExpr body,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprAbs(SourceLocation location, List<String> params, IExpr body) {
        this(location, params, body, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef().value;
    }
}
