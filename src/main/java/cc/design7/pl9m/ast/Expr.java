package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;

public sealed interface Expr permits
        ExprLitInt,
        ExprLitFloat,
        ExprLitBool
{
    Type type();

    default void setType(Type type) {
        throw new UnsupportedOperationException("setType() not supported");
    }

    default boolean needQuote() {
        return false;
    }
}
