package cc.design7.pl9m.tyck;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public record TypeEnv(
        HashMap<Greek, Long> greekTimestamps,
        @Nullable TypeEnv parent,
        @Nullable Type returnType,
        @Nullable Type loopBreakType,
        HashMap<String, TypeScheme> vars,
        HashSet<TypeVar> nonGenericTypeVars
) {
    public TypeEnv(
            @Nullable TypeEnv parent,
            @Nullable Type returnType,
            @Nullable Type loopBreakType
    ) {
        this(
                parent != null ? parent.greekTimestamps : new HashMap<>(),
                parent,
                returnType,
                loopBreakType,
                new HashMap<>(),
                new HashSet<>()
        );
    }

    public @Nullable TypeScheme lookup(String varName) {
        TypeScheme scheme = vars.get(varName);
        if (scheme != null) {
            return scheme;
        }

        if (parent != null) {
            return parent.lookup(varName);
        }

        return null;
    }

    public void collectTypeVars(List<TypeVar> typeVars) {
        for (TypeScheme scheme : vars.values()) {
            scheme.type().collectTypeVars(typeVars);
        }

        if (parent != null) {
            parent.collectTypeVars(typeVars);
        }
    }

    public boolean isNonGeneric(TypeVar var) {
        if (nonGenericTypeVars.contains(var)) {
            return true;
        }

        return parent != null && parent.isNonGeneric(var);
    }

    public @Nullable TypeVar closestReturnType() {
        if (returnType instanceof TypeVar typeVar) {
            return typeVar;
        }

        return parent != null ? parent.closestReturnType() : null;
    }

    public @Nullable TypeVar closestLoopBreakType() {
        if (loopBreakType instanceof TypeVar typeVar) {
            return typeVar;
        }

        if (returnType != null) {
            return null;
        }

        return parent != null ? parent.closestLoopBreakType() : null;
    }
}
