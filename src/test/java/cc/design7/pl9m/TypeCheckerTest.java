package cc.design7.pl9m;

import cc.design7.pl9m.ast.*;
import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.tyck.AlgorithmJ;
import cc.design7.pl9m.tyck.Type;
import cc.design7.pl9m.tyck.TypeEnv;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class TypeCheckerTest {
    @Test
    void test1() throws Exception {
        // let id = \x. x in (id id) (id id)
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

        Type type = AlgorithmJ.J(new TypeEnv(null, null, null), expr);
        System.out.println(type);
    }
}
