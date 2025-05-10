package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;

public record ParseContext(int index, byte[] bytes, SourceLocation location) {
}
