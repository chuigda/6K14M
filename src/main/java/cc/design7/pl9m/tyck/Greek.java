package cc.design7.pl9m.tyck;

public enum Greek {
    ALPHA,
    BETA,
    GAMMA,
    DELTA,
    EPSILON,
    PI,
    SIGMA,
    TAU,
    ETA;

    @Override
    public String toString() {
        return switch (this) {
            case ALPHA -> "α";
            case BETA -> "β";
            case GAMMA -> "γ";
            case DELTA -> "δ";
            case EPSILON -> "ε";
            case PI -> "π";
            case SIGMA -> "σ";
            case TAU -> "τ";
            case ETA -> "η";
        };
    }
}
