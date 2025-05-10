package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.util.Pair;
import org.jetbrains.annotations.Nullable;

public record ParseContext(int index, byte[] bytes, SourceLocation location) {
    public Pair<Token, ParseContext> nextToken() {
        return nextTokenImpl(
                bytes,
                index,
                location.file(),
                location.line(),
                location.col()
        );
    }

    public static Pair<Token, ParseContext> nextTokenImpl(
            byte[] bytes,
            int index,
            @Nullable String file,
            int line,
            int col
    ) {

    }
}
