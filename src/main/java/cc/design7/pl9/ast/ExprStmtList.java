package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

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
