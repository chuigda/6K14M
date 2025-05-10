package cc.design7.pl9m.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Objects;

public final class Drill {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException {
        Class<?> clazz = Class.forName("java.util.ArrayList");
        System.out.println(clazz.getCanonicalName());
        Constructor<?> ctor = clazz.getConstructor(int.class);
        System.out.println(ctor);

        MethodHandle handle = MethodHandles.lookup().findStatic(
                Objects.class,
                "toString",
                MethodType.methodType(String.class, Object.class)
        );
        System.out.println(handle);
    }
}
