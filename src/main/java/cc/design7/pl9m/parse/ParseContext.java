package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;
import cc.design7.pl9m.util.Pair; // 虽然此函数不直接使用，但它是ParseContext上下文的一部分
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
        return null;
    }

    public static int[] skipWhitespace(
            byte[] bytes,
            int startIndex,
            @Nullable String file,
            int startLine,
            int startCol
    ) throws ParseException {
        int index = startIndex;
        int line = startLine;
        int col = startCol;

        while (true) {
            if (index >= bytes.length) {
                break; // 到达文件末尾
            }

            byte b = bytes[index];

            // 1. 处理标准空白字符
            if (b == ' ' || b == '\t') {
                index++;
                col++;
            } else if (b == '\n') { // LF
                index++;
                line++;
                col = 1;
            } else if (b == '\r') { // CR or CRLF
                index++;
                if (index < bytes.length && bytes[index] == '\n') { // CRLF
                    index++;
                }
                line++;
                col = 1;
            }
            // 2. 处理单行注释 '#'
            else if (b == '#') {
                index++; // 跳过 '#'
                col++;
                // 跳过直到行尾或文件尾
                while (index < bytes.length) {
                    byte commentChar = bytes[index];
                    if (commentChar == '\n' || commentChar == '\r') {
                        // 换行符将在下一次外层循环中处理
                        break;
                    }
                    index++;
                    col++;
                }
            }
            // 3. 处理块注释 '(* ... *)'
            else if (b == '(' && (index + 1 < bytes.length) && bytes[index + 1] == '*') {
                int commentStartLine = line;
                int commentStartCol = col;

                index += 2; // 跳过 "(*"
                col += 2;
                int nestingLevel = 1;

                while (nestingLevel > 0) {
                    if (index >= bytes.length) {
                        // 未闭合的块注释
                        String locationString;
                        if (file != null && !file.isEmpty()) {
                            locationString = file + ":" + commentStartLine + ":" + commentStartCol;
                        } else {
                            locationString = "line " + commentStartLine + ", column " + commentStartCol;
                        }
                        throw new ParseException(
                                new SourceLocation(file, commentStartLine, commentStartCol),
                                "Unterminated block comment that started at " + locationString
                        );
                    }

                    byte charInComment = bytes[index];

                    // 检查嵌套的 "(*"
                    if (charInComment == '(' && (index + 1 < bytes.length) && bytes[index + 1] == '*') {
                        index += 2;
                        col += 2;
                        nestingLevel++;
                    }
                    // 检查块注释结束 "*)"
                    else if (charInComment == '*' && (index + 1 < bytes.length) && bytes[index + 1] == ')') {
                        index += 2;
                        col += 2;
                        nestingLevel--;
                    }
                    // 处理注释中的换行
                    else if (charInComment == '\n') {
                        index++;
                        line++;
                        col = 1;
                    } else if (charInComment == '\r') {
                        index++;
                        if (index < bytes.length && bytes[index] == '\n') { // CRLF
                            index++;
                        }
                        line++;
                        col = 1;
                    }
                    // 注释中的其他字符
                    else {
                        index++;
                        col++;
                    }
                }
            }
            else {
                break;
            }
        }

        return new int[]{index, line, col};
    }
}
