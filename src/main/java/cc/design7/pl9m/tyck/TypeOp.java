package cc.design7.pl9m.tyck;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record TypeOp(String op, List<Type> args) implements Type {
    @Override
    public boolean containsTypeVar(TypeVar typeVar) {
        for (Type arg : args) {
            if (arg.containsTypeVar(typeVar)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void collectTypeVars(List<TypeVar> dest) {
        for (Type arg : args) {
            arg.collectTypeVars(dest);
        }
    }

    @Override
    public Type instantiate(Map<TypeVar, TypeVar> freeVars) {
        if (args.isEmpty()) {
            return this;
        }

        return new TypeOp(op, args
                .stream()
                .map(arg -> arg.instantiate(freeVars))
                .toList());
    }

    @Override
    public Type prune() {
        if (args.isEmpty()) {
            return this;
        }

        for (int i = 0; i < args.size(); i++) {
            Type arg = args.get(i);
            Type pruned = arg.prune();
            if (pruned != arg) {
                args.set(i, pruned);
            }
        }

        return this;
    }

    @Override
    public boolean needQuote() {
        return !args.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Algorithm J should not compare deep-equality of type operators");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Algorithm J should not rely on hashCode of type operators");
    }

    @Override
    public @NotNull String toString() {
        if (args.isEmpty()) {
            if (op.equals("*")) {
                return "()";
            }
            return op;
        }

        StringBuilder sb = new StringBuilder();
        if (this.op.equals("*") || this.op.equals("->")) {
            for (int i = 0; i < args.size(); i++) {
                Type arg = args.get(i);
                if (arg.needQuote()) {
                    sb.append('(').append(arg).append(')');
                } else {
                    sb.append(arg);
                }

                if (i < args.size() - 1) {
                    if (op.equals("*")) {
                        sb.append(" × ");
                    } else {
                        sb.append(" -> ");
                    }
                }
            }
        } else {
            sb.append(op).append(' ');
            for (int i = 0; i < args.size(); i++) {
                Type arg = args.get(i);
                if (arg.needQuote()) {
                    sb.append('(').append(arg).append(')');
                } else {
                    sb.append(arg);
                }

                if (i < args.size() - 1) {
                    sb.append(" ");
                }
            }
        }

        return sb.toString();
    }

    public static final TypeOp UNIT_TYPE = new TypeOp("*", List.of());
    public static final TypeOp INT_TYPE = new TypeOp("int", List.of());
    public static final TypeOp FLOAT_TYPE = new TypeOp("float", List.of());
    public static final TypeOp BOOL_TYPE = new TypeOp("bool", List.of());
    public static final TypeOp STRING_TYPE = new TypeOp("string", List.of());

    @Contract("_ -> new")
    public static @NotNull TypeOp productType(List<Type> args) {
        if (args.isEmpty()) {
            return UNIT_TYPE;
        }

        return new TypeOp("*", args);
    }

    @Contract("_ -> new")
    public static @NotNull TypeOp functionType(List<Type> args) {
        return new TypeOp("->", args);
    }
}
