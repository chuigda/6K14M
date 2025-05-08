package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.tyck.TypeOp;

public record ExprLitBool(SourceLocation location, boolean value) implements IExpr {
    @Override
    public IType type() {
        return TypeOp.BOOL_TYPE;
    }
}
