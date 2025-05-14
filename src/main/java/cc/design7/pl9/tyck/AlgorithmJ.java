package cc.design7.pl9.tyck;

import cc.design7.pl9.ast.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AlgorithmJ {
    public static IType J(TypeEnv env, IExpr expr) throws TyckException {
        IType type = jImpl(env, expr).prune();
        if (expr instanceof ITypeResolvable typeResolvable) {
            typeResolvable.typeRef().value = type;
        }
        return type;
    }

    private static IType jImpl(TypeEnv env, IExpr expr) throws TyckException {
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

    private static IType jExprAbs(TypeEnv env, ExprAbs exprAbs) throws TyckException {
        List<IType> functionTypeArgs = new ArrayList<>();

        TypeVar returnType = new TypeVar(Greek.ETA, env.greekTimestamps());

        TypeEnv env1 = new TypeEnv(env, returnType, /*loopBreakType=*/null);
        for (String param : exprAbs.params()) {
            TypeVar beta = new TypeVar(Greek.BETA, env.greekTimestamps());
            env1.vars().put(param, new TypeScheme(List.of(), beta));
            env1.nonGenericTypeVars().add(beta);
            functionTypeArgs.add(beta);
        }

        IType t1 = J(env1, exprAbs.body());
        Unify.unify(returnType, t1);

        functionTypeArgs.add(t1);
        return TypeOp.functionType(functionTypeArgs);
    }

    private static IType jExprApply(TypeEnv env, ExprApp exprApply) throws TyckException {
        IType pi = new TypeVar(Greek.PI, env.greekTimestamps());

        IType fnType = J(env, exprApply.fn());
        List<IType> functionTypeArgs = new ArrayList<>();
        for (IExpr arg : exprApply.args()) {
            functionTypeArgs.add(J(env, arg));
        }
        functionTypeArgs.add(pi);

        Unify.unify(TypeOp.functionType(functionTypeArgs), fnType);
        return pi;
    }

    private static IType jExprIf(TypeEnv env, ExprIf exprIf) throws TyckException {
        IType condType = J(env, exprIf.cond());
        IType thenType = J(env, exprIf.then());

        @Nullable IType otherwiseType = null;
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

    private static IType jExprLetRec(TypeEnv env, ExprLet exprLetRec) throws TyckException {
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
            IType actualType = J(env1, expr);
            Unify.unify(typeVars.get(i), actualType);
        }

        for (int i = 0; i < exprLetRec.vars().size(); i++) {
            String varName = exprLetRec.vars().get(i);
            env1.vars().put(varName, generalize(env1, typeVars.get(i)));
        }

        return J(env1, exprLetRec.body());
    }

    private static IType jExprLet(TypeEnv env, ExprLet exprLet) throws TyckException {
        TypeEnv env1 = new TypeEnv(env, null, null);

        List<TypeScheme> typeSchemes = new ArrayList<>();
        for (IExpr expr : exprLet.values()) {
            IType t = J(env1, expr);
            typeSchemes.add(generalize(env1, t));
        }

        for (int i = 0; i < exprLet.vars().size(); i++) {
            String varName = exprLet.vars().get(i);
            TypeScheme typeScheme = typeSchemes.get(i);
            env1.vars().put(varName, typeScheme);
        }

        return J(env1, exprLet.body());
    }

    private static IType jExprLoop(TypeEnv env, ExprLoop exprLoop) throws TyckException {
        TypeVar loopBreakType = new TypeVar(Greek.ETA, env.greekTimestamps());
        TypeEnv env1 = new TypeEnv(env, /*returnType=*/null, loopBreakType);
        J(env1, exprLoop.body());
        return loopBreakType;
    }

    private static IType jExprReturn(TypeEnv env, ExprReturn exprReturn) throws TyckException {
        @Nullable IType returnType = env.closestReturnType();
        if (returnType == null) {
            throw new TyckException("错误: 在非函数上下文中使用 return 表达式");
        }

        IType actualRetType = exprReturn.value() != null
                ? J(env, exprReturn.value())
                : TypeOp.UNIT_TYPE;
        Unify.unify(returnType, actualRetType);
        return new TypeVar(Greek.ETA, env.greekTimestamps());
    }

    private static IType jExprBreak(TypeEnv env, ExprBreak exprBreak) throws TyckException {
        @Nullable IType loopBreakType = env.closestLoopBreakType();
        if (loopBreakType == null) {
            throw new TyckException("错误: 在非循环上下文中使用 break 表达式");
        }

        IType actualBreakType = exprBreak.value() != null
                ? J(env, exprBreak.value())
                : TypeOp.UNIT_TYPE;
        Unify.unify(loopBreakType, actualBreakType);
        return new TypeVar(Greek.ETA, env.greekTimestamps());
    }

    private static IType jExprStmtList(
            TypeEnv env,
            ExprStmtList exprStmtList
    ) throws TyckException {
        IType lastType = TypeOp.UNIT_TYPE;
        for (IExpr expr : exprStmtList.stmtList()) {
            lastType = J(env, expr);
        }
        return lastType;
    }

    private static IType jExprVar(TypeEnv env, ExprVar exprVar) throws TyckException {
        String varName = exprVar.varName();
        TypeScheme typeScheme = env.lookup(varName);
        if (typeScheme == null) {
            throw new TyckException("错误: 未定义变量 %s".formatted(varName));
        }

        return typeScheme.instantiate(env.greekTimestamps());
    }

    private static TypeScheme generalize(TypeEnv env, IType t) {
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

    private AlgorithmJ() {}
}
