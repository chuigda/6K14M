package cc.design7.pl9.parse;

import cc.design7.pl9.syntax.SourceLocation;

public final class ParseException extends Exception {
    public final SourceLocation location;
    public final String message;

    public ParseException(SourceLocation location, String message) {
        super(message);

        this.location = location;
        this.message = message;
    }
}
