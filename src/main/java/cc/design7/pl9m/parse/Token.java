package cc.design7.pl9m.parse;

import cc.design7.pl9m.syntax.SourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Token(Kind kind, SourceLocation location, @Nullable Object value) {
    public enum Kind {
        IDENT("ident"),
        LIT_INT("lit_int"),
        LIT_FLOAT("lit_float"),
        LIT_STRING("lit_string"),

        KW_TRUE("true"),
        KW_FALSE("false"),
        KW_LET("let"),
        KW_REC("rec"),
        KW_IN("in"),
        KW_NATIVE("native"),
        KW_DECL("decl"),
        KW_TYPE("type"),
        KW_BEGIN("begin"),
        KW_IF("if"),
        KW_THEN("then"),
        KW_ELSE("else"),
        KW_LOOP("loop"),
        KW_BREAK("break"),
        KW_CONTINUE("continue"),
        KW_MATCH("match"),
        KW_CASE("case"),
        KW_DEFAULT("default"),
        KW_RETURN("return"),
        KW_LAMBDA("lambda"),
        KW_FN("fn"),
        KW_END("end"),
        KW_INT("int"),
        KW_FLOAT("float"),
        KW_BOOL("bool"),
        KW_STRING("string"),

        SYM_EQ("="),
        SYM_DCOLON("::"),
        SYM_LPAREN("("),
        SYM_RPAREN(")"),
        SYM_ARROW("->"),
        SYM_PIPE("|"),
        SYM_COMMA(","),
        SYM_LT("<"),
        SYM_GT(">"),
        SYM_LE("<="),
        SYM_GE(">="),
        SYM_F_LT("f<"),
        SYM_F_GT("f>"),
        SYM_F_LE("f<="),
        SYM_F_GE("f>="),
        SYM_DOT("."),
        SYM_LBRACKET("["),
        SYM_RBRACKET("]"),
        SYM_PLUS("+"),
        SYM_MINUS("-"),
        SYM_TILDE("~"),
        SYM_EXCLAIM("!"),
        SYM_F_PLUS("f+"),
        SYM_F_MINUS("f-"),
        SYM_ASTER("*"),
        SYM_SLASH("/"),
        SYM_PERCENT("%"),
        SYM_F_ASTER("f*"),
        SYM_F_SLASH("f/"),
        SYM_F_PERCENT("%"),
        SYM_DLT("<<"),
        SYM_DGT(">>"),
        SYM_TGT(">>>"),
        SYM_DEQ("=="),
        SYM_NEQ("!="),
        SYM_AMP("&"),
        SYM_CARET("^"),
        SYM_DAMP("&&"),
        SYM_DPIPE("||"),
        SYM_COLONEQ(":="),

        EOI("$end_of_input$");

        public final String value;
        Kind(String value) { this.value = value; }
    }

    public Token(Kind kind, SourceLocation location) {
        this(kind, location, null);
    }

    @Override
    public @NotNull String toString() {
        if (value != null) {
            return "<" + kind.value + ", " + value + ">";
        } else {
            return "<" + kind.value + ">";
        }
    }
}
