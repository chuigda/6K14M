package cc.design7.pl9m.test;

import cc.design7.pl9m.ast.*;
import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.AlgorithmJ;
import cc.design7.pl9m.tyck.IType;
import cc.design7.pl9m.tyck.TypeEnv;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class TypeCheckerTest {
    @Test
    void testBasic() throws Exception {
        IExpr expr = new ExprLet(
                SourceLocation.DUMMY,
                false,
                List.of("id"),
                List.of(new ExprAbs(
                        SourceLocation.DUMMY,
                        List.of("x"),
                        new ExprVar(SourceLocation.DUMMY, "x")
                )),
                new ExprApp(
                        SourceLocation.DUMMY,
                        new ExprApp(
                                SourceLocation.DUMMY,
                                new ExprVar(SourceLocation.DUMMY, "id"),
                                List.of(new ExprVar(SourceLocation.DUMMY, "id"))
                        ),
                        List.of(new ExprApp(
                                SourceLocation.DUMMY,
                                new ExprVar(SourceLocation.DUMMY, "id"),
                                List.of(new ExprVar(SourceLocation.DUMMY, "id"))
                        ))
                )
        );

        IType type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println("J(Γ, let id = \\x. x in (id id) (id id)) = " + type);
    }

    @Test
    void testId() throws Exception {
        IExpr expr = new ExprLet(
                SourceLocation.DUMMY,
                false,
                List.of("second"),
                List.of(new ExprAbs(
                        SourceLocation.DUMMY,
                        List.of("x", "y"),
                        new ExprVar(SourceLocation.DUMMY, "y")
                )),
                new ExprVar(SourceLocation.DUMMY, "second")
        );

        IType type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println("J(Γ, let second = \\x. \\y. y in second) = " + type);
    }

    @Test
    void testLoop() throws Exception {
        IExpr expr = new ExprAbs(
                SourceLocation.DUMMY,
                List.of("x"),
                new ExprLoop(SourceLocation.DUMMY, new ExprStmtList(SourceLocation.DUMMY, List.of()))
        );

        IType type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println("J(Γ, \\x. loop pass end) = " + type);
    }

    @Test
    void testLoop2() throws Exception {
        IExpr expr = new ExprAbs(
                SourceLocation.DUMMY,
                List.of("x"),
                new ExprLoop(
                        SourceLocation.DUMMY,
                        new ExprBreak(
                                SourceLocation.DUMMY,
                                new ExprVar(SourceLocation.DUMMY, "x")
                        )
                )
        );

        IType type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println("J(Γ, \\x. loop break x end) = " + type);
    }

    @Test
    void testLoop3() throws Exception {
        IExpr expr = new ExprAbs(
                SourceLocation.DUMMY,
                List.of("x", "y", "z"),
                new ExprLoop(
                        SourceLocation.DUMMY,
                        new ExprIf(
                                SourceLocation.DUMMY,
                                new ExprVar(SourceLocation.DUMMY, "x"),
                                new ExprBreak(
                                        SourceLocation.DUMMY,
                                        new ExprVar(SourceLocation.DUMMY, "y")
                                ),
                                new ExprBreak(
                                        SourceLocation.DUMMY,
                                        new ExprVar(SourceLocation.DUMMY, "z")
                                )
                        )
                )
        );

        IType type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println("J(Γ, \\x y z. loop if x break y else break z end) = " + type);
    }
}
