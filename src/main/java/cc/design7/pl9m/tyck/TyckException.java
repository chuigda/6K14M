package cc.design7.pl9m.tyck;

import java.util.ArrayList;

public final class TyckException extends Exception{
    public final String message;
    public final ArrayList<String> chain;

    public TyckException(String message) {
        this.message = message;
        this.chain = new ArrayList<>();
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = chain.size() - 1; i >= 0; i--) {
            sb.append(chain.get(i));
            if (i != 0) {
                sb.append("\n");
            }
        }
        sb.append(message);

        return sb.toString();
    }
}
