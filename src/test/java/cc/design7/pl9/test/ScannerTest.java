package cc.design7.pl9.test;

import cc.design7.pl9.parse.ParseContext;
import cc.design7.pl9.parse.ParseException;
import cc.design7.pl9.parse.Token;
import cc.design7.pl9.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public final class ScannerTest {
    private static final byte[] FILE_CONTENT;
    static {
        try (InputStream s = ScannerTest.class.getResourceAsStream("/resources/pl9/example1.pl9")) {
            if (s == null) {
                throw new IOException("File not found");
            }

            FILE_CONTENT = s.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test1() throws ParseException {
        ParseContext ctx = new ParseContext(FILE_CONTENT, "example1.pl9");

        Pair<Token, ParseContext> parseResult;
        do {
            parseResult = ctx.nextToken();
            ctx = parseResult.second();

            System.out.println(parseResult.first());
        } while (parseResult.first().kind() != Token.Kind.EOI);
    }
}
