package cc.design7.pl9m.tyck;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public record TypeScheme(List<TypeVar> free, IType type) {
    public IType instantiate(HashMap<Greek, Long> greekTimestamps) {
        HashMap<TypeVar, TypeVar> free = new HashMap<>();
        for (TypeVar var : this.free) {
            free.put(var, new TypeVar(var.greek, greekTimestamps));
        }
        return type.instantiate(free);
    }

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
