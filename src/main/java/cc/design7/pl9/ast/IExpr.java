package cc.design7.pl9.ast;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.tyck.IType;

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
