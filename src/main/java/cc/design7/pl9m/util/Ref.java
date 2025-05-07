package cc.design7.pl9m.util;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Ref<T> {
    public @Nullable T value;

    public Ref() {
        this.value = null;
    }

    public Ref(@Nullable T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ref<?> ref = (Ref<?>) obj;

        return Objects.equals(value, ref.value);
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }
}
