package cc.design7.pl9m.syntax;

import java.util.List;

public record Program(String unitName, String unitPath, List<IDecl> declList) {}
