package cc.design7.pl9.parse;

import cc.design7.pl9.syntax.SourceLocation;
import cc.design7.pl9.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public record ParseContext(int index, byte[] bytes, SourceLocation location) {
    public ParseContext(byte[] bytes, String fileName) {
        this(0, bytes, new SourceLocation(fileName, 1, 1));
    }

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
        } else if (b == '"' || b == '\'') {
            return scanString(bytes, index, file, line, col);
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

        SourceLocation endLocation = new SourceLocation(file, line, col);

        String ident = new String(bytes, start, index - start);
        Token.Kind kind = KEYWORD_MAP.get(ident);
        if (kind == null) {
            kind = Token.Kind.IDENT;
            return new Pair<>(
                    new Token(kind, location, ident),
                    new ParseContext(index, bytes, endLocation)
            );
        } else {
            return new Pair<>(
                    new Token(kind, location),
                    new ParseContext(index, bytes, endLocation)
            );
        }
    }

    private static Pair<Token, ParseContext> scanNumber(
            byte[] bytes,
            int index,
            @Nullable String file,
            int line,
            int col
    ) throws ParseException {
        SourceLocation location = new SourceLocation(file, line, col);
        int startIndex = index;
        int currentCol = col;

        while (index < bytes.length && bytes[index] >= '0' && bytes[index] <= '9') {
            index++;
            currentCol++;
        }

        boolean isFloat = false;
        if (index < bytes.length && bytes[index] == '.') {
            if (index + 1 < bytes.length && bytes[index + 1] >= '0' && bytes[index + 1] <= '9') {
                isFloat = true;
                do {
                    index++;
                    currentCol++;
                } while (index < bytes.length && bytes[index] >= '0' && bytes[index] <= '9');
            }
        }

        String numStr = new String(bytes, startIndex, index - startIndex);
        SourceLocation endLocation = new SourceLocation(file, line, currentCol);
        ParseContext newContext = new ParseContext(index, bytes, endLocation);

        if (isFloat) {
            try {
                double value = Double.parseDouble(numStr);
                return new Pair<>(new Token(Token.Kind.LIT_FLOAT, location, value), newContext);
            } catch (NumberFormatException e) {
                throw new ParseException(location, "Invalid float literal: " + numStr);
            }
        } else {
            try {
                long value = Long.parseLong(numStr);
                return new Pair<>(new Token(Token.Kind.LIT_INT, location, value), newContext);
            } catch (NumberFormatException e) {
                throw new ParseException(location, "Invalid or too large integer literal: " + numStr);
            }
        }
    }

    private static Pair<Token, ParseContext> scanString(
            byte[] bytes,
            int index,
            @Nullable String file,
            int line,
            int col
    ) throws ParseException {
        SourceLocation location = new SourceLocation(file, line, col);
        byte quoteChar = bytes[index];
        index++;
        int currentCol = col + 1;
        int contentStartIndex = index;

        while (index < bytes.length) {
            byte b = bytes[index];
            if (b == '\\') {
                if (index + 1 >= bytes.length) {
                    throw new ParseException(location, "Unterminated string literal (escape sequence at end of file)");
                }

                switch (bytes[index + 1]) {
                    case 'n', 'r', 't', '\\', '\'', '"' -> {
                        index += 2;
                        currentCol += 2;
                    }
                    default -> throw new ParseException(location, "Invalid escape sequence: \\" + (char) bytes[index + 1]);
                }
            } else if (b == quoteChar) {
                String value = new String(bytes, contentStartIndex, index - contentStartIndex);
                index++;
                currentCol++;
                SourceLocation endLocation = new SourceLocation(file, line, currentCol);
                ParseContext newContext = new ParseContext(index, bytes, endLocation);
                return new Pair<>(new Token(Token.Kind.LIT_STRING, location, value), newContext);
            } else if (b == '\n' || b == '\r') {
                SourceLocation errLoc = new SourceLocation(file, line, currentCol);
                throw new ParseException(errLoc, "Unterminated string literal (newline in string)");
            }
            index++;
            currentCol++;
        }
        throw new ParseException(location, "Unterminated string literal");
    }

    private static Pair<Token, ParseContext> scanSymbol(
            byte[] bytes,
            int index,
            @Nullable String file,
            int line,
            int col
    ) throws ParseException {
        SourceLocation location = new SourceLocation(file, line, col);
        byte b1 = bytes[index];
        @Nullable Token.Kind kind = null;
        int len = 1;

        switch (b1) {
            case '=':
                if (index + 1 < bytes.length && bytes[index + 1] == '=') {
                    kind = Token.Kind.SYM_DEQ; len = 2;
                } else {
                    kind = Token.Kind.SYM_EQ;
                }
                break;
            case ':':
                if (index + 1 < bytes.length) {
                    if (bytes[index + 1] == ':') {
                        kind = Token.Kind.SYM_DCOLON; len = 2;
                    } else if (bytes[index + 1] == '=') {
                        kind = Token.Kind.SYM_COLONEQ; len = 2;
                    }
                }
                break;
            case '(': kind = Token.Kind.SYM_LPAREN; break;
            case ')': kind = Token.Kind.SYM_RPAREN; break;
            case '-':
                if (index + 1 < bytes.length && bytes[index + 1] == '>') {
                    kind = Token.Kind.SYM_ARROW; len = 2;
                } else {
                    kind = Token.Kind.SYM_MINUS;
                }
                break;
            case '|':
                if (index + 1 < bytes.length && bytes[index + 1] == '|') {
                    kind = Token.Kind.SYM_DPIPE; len = 2;
                } else {
                    kind = Token.Kind.SYM_PIPE;
                }
                break;
            case ',': kind = Token.Kind.SYM_COMMA; break;
            case ';': kind = Token.Kind.SYM_SEMICOLON; break;
            case '<':
                if (index + 1 < bytes.length) {
                    if (bytes[index + 1] == '=') {
                        kind = Token.Kind.SYM_LE; len = 2;
                    } else if (bytes[index + 1] == '<') {
                        kind = Token.Kind.SYM_DLT; len = 2;
                    } else {
                        kind = Token.Kind.SYM_LT;
                    }
                } else {
                    kind = Token.Kind.SYM_LT;
                }
                break;
            case '>':
                if (index + 1 < bytes.length) {
                    if (bytes[index + 1] == '=') {
                        kind = Token.Kind.SYM_GE; len = 2;
                    } else if (bytes[index + 1] == '>') {
                        if (index + 2 < bytes.length && bytes[index + 2] == '>') {
                            kind = Token.Kind.SYM_TGT; len = 3;
                        } else {
                            kind = Token.Kind.SYM_DGT; len = 2;
                        }
                    } else {
                        kind = Token.Kind.SYM_GT;
                    }
                } else {
                    kind = Token.Kind.SYM_GT;
                }
                break;
            case '.': kind = Token.Kind.SYM_DOT; break;
            case '[': kind = Token.Kind.SYM_LBRACKET; break;
            case ']': kind = Token.Kind.SYM_RBRACKET; break;
            case '+': kind = Token.Kind.SYM_PLUS; break;
            case '~': kind = Token.Kind.SYM_TILDE; break;
            case '!':
                if (index + 1 < bytes.length && bytes[index + 1] == '=') {
                    kind = Token.Kind.SYM_NEQ; len = 2;
                } else {
                    kind = Token.Kind.SYM_EXCLAIM;
                }
                break;
            case '*': kind = Token.Kind.SYM_ASTER; break;
            case '/': kind = Token.Kind.SYM_SLASH; break;
            case '%': kind = Token.Kind.SYM_PERCENT; break;
            case '&':
                if (index + 1 < bytes.length && bytes[index + 1] == '&') {
                    kind = Token.Kind.SYM_DAMP; len = 2;
                } else {
                    kind = Token.Kind.SYM_AMP;
                }
                break;
            case '^': kind = Token.Kind.SYM_CARET; break;
        }

        if (kind != null) {
            SourceLocation endLocation = new SourceLocation(file, line, col + len);
            ParseContext newContext = new ParseContext(index + len, bytes, endLocation);
            return new Pair<>(new Token(kind, location), newContext);
        } else {
            throw new ParseException(location, "Unexpected character: " + (char)b1);
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
