package cc.design7.pl9.syntax;

public sealed interface IDecl permits
        DeclLet,
        DeclNative,
        DeclType,
        DeclNativeType
{
    SourceLocation location();
}
