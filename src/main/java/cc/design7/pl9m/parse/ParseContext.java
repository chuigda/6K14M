package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.util.Pair;
import org.jetbrains.annotations.Nullable;

public record ParseContext(int index, byte[] bytes, SourceLocation location) {
    public Pair<Token, ParseContext> nextToken() throws ParseException {
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
    ) throws ParseException {
        int[] newPos = skipWhitespace(bytes, index, file, line, col);
        index = newPos[0];
        line = newPos[1];
        col = newPos[2];

        if (index >= bytes.length) {
            SourceLocation eoiLocation = new SourceLocation(file, line, col);
            ParseContext context = new ParseContext(index, bytes, eoiLocation);
            return new Pair<>(new Token(Token.Kind.EOI, eoiLocation), context);
        }

        // TODO
    }

    // TODO
}
