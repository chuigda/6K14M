package cc.design7.pl9.syntax;

public record DeclNativeType(
        SourceLocation location,
        String typeName,
        String nativeTypeName
) implements IDecl {}
