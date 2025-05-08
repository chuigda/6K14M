package cc.design7.pl9m.syntax;

public record DeclNative(
        SourceLocation location,
        String functionName,
        String javaFunctionName
) implements IDecl {}
