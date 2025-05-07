package cc.design7.pl9m.tyck;

import java.util.List;
import java.util.Map;

public sealed interface Type permits TypeVar, TypeOp {
    boolean containsTypeVar(TypeVar typeVar);
    void collectTypeVars(List<TypeVar> dest);
    Type instantiate(Map<TypeVar, TypeVar> freeVars);
    Type prune();

    boolean needQuote();
}
