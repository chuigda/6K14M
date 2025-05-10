package cc.design7.pl9m.tyck;

import java.util.ArrayList;

public final class TyckException extends Exception{
    public final String message;
    public final ArrayList<String> chain;

    public TyckException(String message) {
        super(message);
        this.message = message;
        this.chain = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        for (String s : chain) {
            sb.append("\n").append(s);
        }
        return sb.toString();
    }
}
