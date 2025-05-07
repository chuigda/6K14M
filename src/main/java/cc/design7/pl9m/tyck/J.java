package cc.design7.pl9m.tyck;

public final class J {
    public static void unify(Type t1, Type t2) throws TypeCheckException {
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
        } catch (TypeCheckException e) {
            e.chain.add("当归一化类型 %s 和 %s 时发生".formatted(t1, t2));
            throw e;
        }

        throw new TypeCheckException("错误: 无法归一化类型 %s 和 %s".formatted(t1, t2));
    }

    private static void unifyTypeVar(TypeVar typeVar, Type type) throws TypeCheckException {
        if (typeVar == type || (type instanceof TypeVar typeVar2 && typeVar.equals(typeVar2))) {
            return;
        }

        if (type.containsTypeVar(typeVar)) {
            throw new TypeCheckException("错误: 无法归一化类型变量 %s 和类型 %s：后者中存在对前者的引用，这是不允许的".formatted(type, typeVar));
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

    private static void unifyTypeOp(TypeOp typeOp1, TypeOp typeOp2) throws TypeCheckException {
        if (!typeOp1.op().equals(typeOp2.op())) {
            throw new TypeCheckException("错误: 无法归一化类型算子 %s 和 %s（运算符不同）".formatted(typeOp1, typeOp1));
        }

        if (typeOp1.args().size() != typeOp2.args().size()) {
            throw new TypeCheckException("错误: 无法归一化类型算子 %s 和 %s（参数数量不同）".formatted(typeOp1, typeOp2));
        }

        for (int i = 0; i < typeOp1.args().size(); i++) {
            Type arg1 = typeOp1.args().get(i);
            Type arg2 = typeOp2.args().get(i);

            try {
                unify(arg1, arg2);
            } catch (TypeCheckException e) {
                e.chain.add("当归一化类型算子 %s 的第 %d 个参数 （%s 和 %s）时发生".formatted(typeOp1.op(), i, arg1, arg2));
                throw e;
            }
        }
    }
}
