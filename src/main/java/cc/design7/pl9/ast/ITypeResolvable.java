package cc.design7.pl9.ast;

import cc.design7.pl9.tyck.IType;
import cc.design7.pl9.util.Ref;

public interface ITypeResolvable {
    Ref<IType> typeRef();

    default void setType(IType type) {
        typeRef().value = type;
    }
}
