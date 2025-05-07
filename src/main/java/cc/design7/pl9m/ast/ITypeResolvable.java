package cc.design7.pl9m.ast;

import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.util.Ref;

public interface ITypeResolvable {
    Ref<Type> typeRef();

    default void setType(Type type) {
        typeRef().value = type;
    }
}
