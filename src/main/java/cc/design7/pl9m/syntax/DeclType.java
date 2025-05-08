package cc.design7.pl9m.syntax;

import java.util.List;

public record DeclType(
        SourceLocation location,
        String typeName,
        List<Constructor> constructors
) implements IDecl {
    public record Constructor(String name, List<String> args) {}
}
