package cc.design7.pl9m.syntax;

import cc.design7.pl9m.ast.IExpr;

import java.util.List;

public record DeclLet(
        SourceLocation location,
        boolean rec,
        List<String> vars,
        List<IExpr> values
) implements IDecl {}
