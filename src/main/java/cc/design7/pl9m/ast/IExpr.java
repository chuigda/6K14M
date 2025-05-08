package cc.design7.pl9m.ast;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.IType;

public sealed interface IExpr permits
        ExprLitInt,
        ExprLitFloat,
        ExprLitBool,
        ExprLitString,
        ExprVar,
        ExprAbs,
        ExprApp,
        ExprLet,
        ExprStmtList,
        ExprReturn,
        ExprBreak,
        ExprContinue,
        ExprIf,
        ExprLoop
{
    SourceLocation location();
    IType type();

    default boolean needQuote() {
        return false;
    }
}
