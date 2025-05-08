package cc.design7.pl9m.syntax;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SourceLocation(@Nullable String file, int line, int col) {
    public static final SourceLocation DUMMY = new SourceLocation(null, -1, -1);

    public @NotNull String file() {
        return file != null ? file : "<unknown>";
    }

    public boolean isDummy() { return line == -1; }
}
