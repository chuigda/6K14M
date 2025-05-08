package cc.design7.pl9m.tyck;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TypeVar implements IType {
    public final Greek greek;
    public final long timestamp;
    public @Nullable IType resolve;

    public TypeVar(Greek greek, HashMap<Greek, Long> greekTimestamps) {
        long timestamp = greekTimestamps.getOrDefault(greek, 0L);
        greekTimestamps.put(greek, timestamp + 1);

        this.greek = greek;
        this.timestamp = timestamp;
        this.resolve = null;
    }

    @Override
    public boolean containsTypeVar(TypeVar typeVar) {
        return this.equals(typeVar);
    }

    @Override
    public void collectTypeVars(List<TypeVar> dest) {
        dest.add(this);
    }

    @Override
    public IType instantiate(Map<TypeVar, TypeVar> freeVars) {
        return freeVars.getOrDefault(this, this);
    }

    @Override
    public IType prune() {
        if (this.resolve != null) {
            IType pruned = this.resolve.prune();
            this.resolve = pruned;
            return pruned;
        } else {
            return this;
        }
    }

    @Override
    public boolean needQuote() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TypeVar other)) return false;

        return greek.equals(other.greek) && timestamp == other.timestamp;
    }

    @Override
    public int hashCode() {
        return greek.hashCode() + Long.hashCode(timestamp);
    }

    @Override
    public String toString() {
        if (this.greek == Greek.ETA) {
            return "!";
        }

        return this.greek.toString() + this.timestamp;
    }
}
