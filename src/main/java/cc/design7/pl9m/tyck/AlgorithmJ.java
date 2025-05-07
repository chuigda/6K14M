package cc.design7.pl9m.tyck;

import cc.design7.pl9m.ast.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AlgorithmJ {
    public static Type J(TypeEnv env, IExpr expr) throws TypeCheckException {
        Type type = jImpl(env, expr);
        if (expr instanceof ITypeResolvable typeResolvable) {
            typeResolvable.typeRef().value = type.prune();
        }
        return type;
    }

    private static Type jImpl(TypeEnv env, IExpr expr) throws TypeCheckException {
        return switch (expr) {
            case ExprLitBool _ -> TypeOp.BOOL_TYPE;
            case ExprLitFloat _ -> TypeOp.FLOAT_TYPE;
            case ExprLitInt _ -> TypeOp.INT_TYPE;
            case ExprLitString _ -> TypeOp.STRING_TYPE;

            case ExprVar exprVar -> jExprVar(env, exprVar);
            case ExprStmtList exprStmtList -> jExprStmtList(env, exprStmtList);
            case ExprIf exprIf -> jExprIf(env, exprIf);
            case ExprApp exprApp -> jExprApply(env, exprApp);
            case ExprLoop exprLoop -> jExprLoop(env, exprLoop);
            case ExprBreak exprBreak -> jExprBreak(env, exprBreak);
            case ExprContinue _ -> new TypeVar(Greek.ETA, env.greekTimestamps());

            case ExprLet exprLet -> exprLet.rec()
                    ? jExprLetRec(env, exprLet)
                    : jExprLet(env, exprLet);
            case ExprAbs exprAbs -> jExprAbs(env, exprAbs);
            case ExprReturn exprReturn -> jExprReturn(env, exprReturn);
        };
    }

    private static Type jExprAbs(TypeEnv env, ExprAbs exprAbs) throws TypeCheckException {
        List<Type> functionTypeArgs = new ArrayList<>();

        TypeVar returnType = new TypeVar(Greek.ETA, env.greekTimestamps());

        TypeEnv env1 = new TypeEnv(env, returnType, /*loopBreakType=*/null);
        for (String param : exprAbs.params()) {
            TypeVar beta = new TypeVar(Greek.BETA, env.greekTimestamps());
            env1.vars().put(param, new TypeScheme(List.of(), beta));
            env1.nonGenericTypeVars().add(beta);
            functionTypeArgs.add(beta);
        }

        Type t1 = J(env1, exprAbs.body());
        Unify.unify(returnType, t1);

        functionTypeArgs.add(t1);
        return TypeOp.functionType(functionTypeArgs);
    }

    private static Type jExprApply(TypeEnv env, ExprApp exprApply) throws TypeCheckException {
        Type pi = new TypeVar(Greek.PI, env.greekTimestamps());

        Type fnType = J(env, exprApply.fn());
        List<Type> functionTypeArgs = new ArrayList<>();
        for (IExpr arg : exprApply.args()) {
            functionTypeArgs.add(J(env, arg));
        }
        functionTypeArgs.add(pi);

        Unify.unify(TypeOp.functionType(functionTypeArgs), fnType);
        return pi;
    }

    private static Type jExprIf(TypeEnv env, ExprIf exprIf) throws TypeCheckException {
        Type condType = J(env, exprIf.cond());
        Type thenType = J(env, exprIf.then());

        @Nullable Type otherwiseType = null;
        if (exprIf.otherwise() != null) {
            otherwiseType = J(env, exprIf.otherwise());
        }

        Unify.unify(TypeOp.BOOL_TYPE, condType);
        if (otherwiseType != null) {
            Unify.unify(thenType, otherwiseType);
            return thenType;
        } else {
            return TypeOp.UNIT_TYPE;
        }
    }

    private static Type jExprLetRec(TypeEnv env, ExprLet exprLetRec) throws TypeCheckException {
        TypeEnv env1 = new TypeEnv(env, null, null);
        List<TypeVar> typeVars = new ArrayList<>();

        for (String name : exprLetRec.vars()) {
            TypeVar gamma = new TypeVar(Greek.GAMMA, env.greekTimestamps());
            typeVars.add(gamma);

            env1.vars().put(name, new TypeScheme(List.of(), gamma));
            env1.nonGenericTypeVars().add(gamma);
        }

        for (int i = 0; i < exprLetRec.values().size(); i++) {
            IExpr expr = exprLetRec.values().get(i);
            Type actualType = J(env1, expr);
            Unify.unify(typeVars.get(i), actualType);
        }

        for (int i = 0; i < exprLetRec.vars().size(); i++) {
            String varName = exprLetRec.vars().get(i);
            env1.vars().put(varName, generalize(env1, typeVars.get(i)));
        }

        return J(env1, exprLetRec.body());
    }

    private static Type jExprLet(TypeEnv env, ExprLet exprLet) throws TypeCheckException {
        TypeEnv env1 = new TypeEnv(env, null, null);

        List<TypeScheme> typeSchemes = new ArrayList<>();
        for (IExpr expr : exprLet.values()) {
            Type t = J(env1, expr);
            typeSchemes.add(generalize(env1, t));
        }

        for (int i = 0; i < exprLet.vars().size(); i++) {
            String varName = exprLet.vars().get(i);
            TypeScheme typeScheme = typeSchemes.get(i);
            env1.vars().put(varName, typeScheme);
        }

        return J(env1, exprLet.body());
    }

    private static Type jExprLoop(TypeEnv env, ExprLoop exprLoop) throws TypeCheckException {
        TypeVar loopBreakType = new TypeVar(Greek.ETA, env.greekTimestamps());
        TypeEnv env1 = new TypeEnv(env, /*returnType=*/null, loopBreakType);
        return J(env1, exprLoop.body());
    }

    private static Type jExprReturn(TypeEnv env, ExprReturn exprReturn) throws TypeCheckException {
        @Nullable Type returnType = env.closestReturnType();
        if (returnType == null) {
            throw new TypeCheckException("错误: 在非函数上下文中使用 return 表达式");
        }

        Type actualRetType = exprReturn.value() != null
                ? J(env, exprReturn.value())
                : TypeOp.UNIT_TYPE;
        Unify.unify(returnType, actualRetType);
        return new TypeVar(Greek.ETA, env.greekTimestamps());
    }

    private static Type jExprBreak(TypeEnv env, ExprBreak exprBreak) throws TypeCheckException {
        @Nullable Type loopBreakType = env.closestLoopBreakType();
        if (loopBreakType == null) {
            throw new TypeCheckException("错误: 在非循环上下文中使用 break 表达式");
        }

        Type actualBreakType = exprBreak.value() != null
                ? J(env, exprBreak.value())
                : TypeOp.UNIT_TYPE;
        Unify.unify(loopBreakType, actualBreakType);
        return new TypeVar(Greek.ETA, env.greekTimestamps());
    }

    private static Type jExprStmtList(
            TypeEnv env,
            ExprStmtList exprStmtList
    ) throws TypeCheckException {
        Type lastType = TypeOp.UNIT_TYPE;
        for (IExpr expr : exprStmtList.stmtList()) {
            lastType = J(env, expr);
        }
        return lastType;
    }

    private static Type jExprVar(TypeEnv env, ExprVar exprVar) throws TypeCheckException {
        String varName = exprVar.varName();
        TypeScheme typeScheme = env.lookup(varName);
        if (typeScheme == null) {
            throw new TypeCheckException("错误: 未定义变量 %s".formatted(varName));
        }

        return typeScheme.instantiate(env.greekTimestamps());
    }

    private static TypeScheme generalize(TypeEnv env, Type t) {
        t = t.prune();
        List<TypeVar> typeVars = new ArrayList<>();
        t.collectTypeVars(typeVars);

        List<TypeVar> filteredTypeVars = new ArrayList<>();
        for (TypeVar typeVar : typeVars) {
            if (!env.isNonGeneric(typeVar)) {
                filteredTypeVars.add(typeVar);
            }
        }

        return new TypeScheme(filteredTypeVars, t);
    }
}
