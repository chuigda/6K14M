package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.util.Ref;

public interface ITypeResolvable {
    Ref<IType> typeRef();

    default void setType(IType type) {
        typeRef().value = type;
    }
}
