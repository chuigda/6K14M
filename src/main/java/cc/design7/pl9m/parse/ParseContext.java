package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

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
        if (b == 'f' && index + 1 < bytes.length) {
            @Nullable Token.Kind kind = switch (bytes[index + 1]) {
                case '+' -> Token.Kind.SYM_F_PLUS;
                case '-' -> Token.Kind.SYM_F_MINUS;
                case '*' -> Token.Kind.SYM_F_ASTER;
                case '/' -> Token.Kind.SYM_F_SLASH;
                case '%' -> Token.Kind.SYM_F_PERCENT;
                default -> null;
            };

            if (kind != null) {
                SourceLocation tokenLocation = new SourceLocation(file, line, col);
                SourceLocation endLocation = new SourceLocation(file, line, col + 2);

                return new Pair<>(
                        new Token(kind, tokenLocation),
                        new ParseContext(index + 2, bytes, endLocation)
                );
            }
        }

        if (isIdentStart(b)) {
            return scanIdentOrKeyword(bytes, index, file, line, col);
        } else if (isNumericStart(b)) {
            return scanNumber(bytes, index, file, line, col);
        } else {
            return scanSymbol(bytes, index, file, line, col);
        }
    }

    private static Pair<Token, ParseContext> scanIdentOrKeyword(
            byte[] bytes,
            int index,
            @Nullable String file,
            int line,
            int col
    ) {
        SourceLocation location = new SourceLocation(file, line, col);

        int start = index;
        byte b = bytes[index];
        while (isIdentChar(b)) {
            index++;
            col++;
            if (index >= bytes.length) {
                break;
            }
            b = bytes[index];
        }

        SourceLocation endLoaction = new SourceLocation(file, line, col);

        String ident = new String(bytes, start, index - start);
        Token.Kind kind = KEYWORD_MAP.get(ident);
        if (kind == null) {
            kind = Token.Kind.IDENT;
            return new Pair<>(
                    new Token(kind, location, ident),
                    new ParseContext(index, bytes, endLoaction)
            );
        } else {
            return new Pair<>(
                    new Token(kind, location),
                    new ParseContext(index, bytes, endLoaction)
            );
        }
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

    private static boolean isIdentStart(byte b) {
        return (b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || b == '_';
    }

    private static boolean isIdentChar(byte b) {
        return isIdentStart(b) || (b >= '0' && b <= '9') || b == '?' || b == '!' || b == '-' || b == '$';
    }

    private static boolean isNumericStart(byte b) {
        return b >= '0' && b <= '9';
    }

    private static final HashMap<String, Token.Kind> KEYWORD_MAP = new HashMap<>();
    static {
        for (Token.Kind kind : Token.Kind.values()) {
            if (kind.name().startsWith("KW_")) {
                KEYWORD_MAP.put(kind.value, kind);
            }
        }
    }
}
