package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;

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
        ExprIf,
        ExprLoop
{
    SourceLocation location();
    Type type();

    default boolean needQuote() {
        return false;
    }
}
