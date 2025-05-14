package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.tyck.TypeOp;

public record ExprLitString(SourceLocation location, String value) implements IExpr {
    @Override
    public IType type() {
        return TypeOp.STRING_TYPE;
    }
}
