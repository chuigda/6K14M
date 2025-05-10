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
        int[] newPos = skipWhitespace(bytes, index, line, col);
        index = newPos[0];
        line = newPos[1];
        col = newPos[2];

        if (index >= bytes.length) {
            SourceLocation eoiLocation = new SourceLocation(file, line, col);
            ParseContext context = new ParseContext(index, bytes, eoiLocation);
            return new Pair<>(new Token(Token.Kind.EOI, eoiLocation), context);
        }

        byte b = bytes[index];
        return null; // TODO
    }

    private static int[] skipWhitespace(byte[] bytes, int index, int line, int col) {
        while (true) {
            if (index >= bytes.length) {
                break;
            }

            byte b = bytes[index];
            if (b == ' ' || b == '\t') {
                index++;
                col++;
            } else if (b == '\n') {
                index++;
                line++;
                col = 1;
            } else if (b == '\r') {
                index++;
                if (index < bytes.length && bytes[index] == '\n') {
                    index++;
                }
                line++;
                col = 1;
            }
            else if (b == '#') {
                index++;
                col++;
                while (index < bytes.length) {
                    byte commentChar = bytes[index];
                    if (commentChar == '\n' || commentChar == '\r') {
                        break;
                    }
                    index++;
                    col++;
                }
            }
            else {
                break;
            }
        }

        return new int[]{index, line, col};
    }
}
