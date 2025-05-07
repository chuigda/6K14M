package cc.design7.pl9m.tyck;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TypeScheme(List<TypeVar> free, Type type) {
    @Override
    public @NotNull String toString() {
        if (free.isEmpty()) {
            return type.toString();
        }

        StringBuilder sb = new StringBuilder();
        for (TypeVar var : free) {
            if (var.greek != Greek.ETA) {
                sb.append('∀').append(var);
            }
        }
        sb.append(". ").append(type);

        return sb.toString();
    }
}
