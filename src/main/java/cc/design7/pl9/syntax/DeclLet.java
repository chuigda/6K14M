package cc.design7.pl9.syntax;

import cc.design7.pl9.ast.IExpr;

import java.util.List;

public record DeclLet(
        SourceLocation location,
        boolean rec,
        List<String> vars,
        List<IExpr> values
) implements IDecl {}
