package cc.design7.pl9m.syntax;

public record SourceLocation(String file, int line, int col) {
    public static final SourceLocation DUMMY = new SourceLocation("<unknown>", -1, -1);

    public boolean isDummy() { return line == -1; }
}
