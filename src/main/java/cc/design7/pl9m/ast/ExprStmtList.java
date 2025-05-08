package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

import java.util.List;

public record ExprStmtList(
        SourceLocation location,
        List<IExpr> stmtList,
        Ref<IType> typeRef
) implements IExpr, ITypeResolvable {
    public ExprStmtList(SourceLocation location, List<IExpr> stmtList) {
        this(location, stmtList, new Ref<>());
    }

    @Override
    public IType type() {
        return typeRef.value;
    }
}
