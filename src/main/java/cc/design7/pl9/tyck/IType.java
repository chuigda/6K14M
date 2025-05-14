package cc.design7.pl9.tyck;

import java.util.List;
import java.util.Map;

public sealed interface IType permits TypeVar, TypeOp {
    boolean containsTypeVar(TypeVar typeVar);
    void collectTypeVars(List<TypeVar> dest);
    IType instantiate(Map<TypeVar, TypeVar> freeVars);
    IType prune();

    boolean needQuote();
}
