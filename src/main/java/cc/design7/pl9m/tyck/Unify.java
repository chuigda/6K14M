package cc.design7.pl9m.tyck;

public final class Unify {
    /// Due to the imperative nature of the Algorithm J, this `unify` operator is not really
    /// commutative. This function tend to resolve {@code t1} to {@code t2}
    /// ({@code t1.resolve = t2}) when both {@code t1} and {@code t2} are unresolved
    /// {@link TypeVar}s. Be careful with argument order.
    public static void unify(IType t1, IType t2) throws TyckException {
        t1 = t1.prune();
        t2 = t2.prune();

        try {
            if (t1 instanceof TypeOp typeOp1 && t2 instanceof TypeOp typeOp2) {
                unifyTypeOp(typeOp1, typeOp2);
                return;
            } else if (t1 instanceof TypeVar typeVar) {
                unifyTypeVar(typeVar, t2);
                return;
            } else if (t2 instanceof TypeVar typeVar) {
                unifyTypeVar(typeVar, t1);
                return;
            }
        } catch (TyckException e) {
            e.chain.add("当归一化类型 %s 和 %s 时发生".formatted(t1, t2));
            throw e;
        }

        throw new TyckException("错误: 无法归一化类型 %s 和 %s".formatted(t1, t2));
    }

    private static void unifyTypeVar(TypeVar typeVar, IType type) throws TyckException {
        if (typeVar == type || (type instanceof TypeVar typeVar2 && typeVar.equals(typeVar2))) {
            return;
        }

        if (type.containsTypeVar(typeVar)) {
            throw new TyckException("错误: 无法归一化类型变量 %s 和类型 %s：后者中存在对前者的引用，这是不允许的".formatted(typeVar, type));
        }

        if (type instanceof TypeVar typeVar2) {
            if (typeVar.resolve == null) {
                typeVar.resolve = typeVar2;
            } else if (typeVar2.resolve == null) {
                typeVar2.resolve = typeVar;
            } else {
                assert false;
            }
        } else {
            if (typeVar.resolve == null) {
                typeVar.resolve = type;
            } else {
                assert false;
            }
        }
    }

    private static void unifyTypeOp(TypeOp typeOp1, TypeOp typeOp2) throws TyckException {
        if (!typeOp1.op().equals(typeOp2.op())) {
            throw new TyckException("错误: 无法归一化类型算子 %s 和 %s（运算符不同）".formatted(typeOp1, typeOp2));
        }

        if (typeOp1.args().size() != typeOp2.args().size()) {
            throw new TyckException("错误: 无法归一化类型算子 %s 和 %s（参数数量不同）".formatted(typeOp1, typeOp2));
        }

        for (int i = 0; i < typeOp1.args().size(); i++) {
            IType arg1 = typeOp1.args().get(i);
            IType arg2 = typeOp2.args().get(i);

            try {
                unify(arg1, arg2);
            } catch (TyckException e) {
                e.chain.add("当归一化类型算子 %s 的第 %d 个参数 （%s 和 %s）时发生".formatted(typeOp1.op(), i, arg1, arg2));
                throw e;
            }
        }
    }

    private Unify() {}
}
