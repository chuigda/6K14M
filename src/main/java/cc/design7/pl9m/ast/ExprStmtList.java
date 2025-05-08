package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;

import java.util.List;

public record ExprStmtList(
        SourceLocation location,
        List<IExpr> stmtList,
        Ref<Type> typeRef
) implements IExpr, ITypeResolvable {
    public ExprStmtList(SourceLocation location, List<IExpr> stmtList) {
        this(location, stmtList, new Ref<>());
    }

    @Override
    public Type type() {
        return typeRef.value;
    }
}
