package cc.design7.pl9m.jreg;

import cc.design7.pl9m.util.Pair;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;

public record JRegistry(
        HashMap<String, MethodHandle> nativeFunctions,
        HashMap<String, Pair<Class<?>, MethodHandle>> nativeTypes
) {
    public void addNativeFunction(
            String functionName,
            String javaMethodSpec
    ) throws ReflectionException {
    }

    public void addNativeType(
            String typeName,
            String javaTypeName,
            String ctorSpec
    ) throws ReflectionException {
    }
}
