package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.tyck.TypeOp;

public record ExprLitBool(SourceLocation location, boolean value) implements IExpr {
    @Override
    public Type type() {
        return TypeOp.BOOL_TYPE;
    }
}
