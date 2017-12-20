package fga.evo.model.physics;

public class PairBond {
    private final Ball ball1;
    private final Ball ball2;

    public PairBond(Ball ball1, Ball ball2) {
        if (ball1 == null || ball2 == null)
            throw new IllegalArgumentException("Cannot bond null");
        if (ball1 == ball2)
            throw new IllegalArgumentException("Cannot bond a ball to itself");

        this.ball1 = ball1;
        this.ball2 = ball2;
    }

    public boolean bondsTo(Ball ball) {
        return ball1 == ball || ball2 == ball;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof PairBond))
            return false;

        PairBond bond = (PairBond) obj;
        return (bond.ball1 == ball1 && bond.ball2 == ball2) || (bond.ball1 == ball2 && bond.ball2 == ball1);
    }

    @Override
    public int hashCode() {
        // Same algorithm as in java.util.Set#hashCode.
        return ball1.hashCode() + ball2.hashCode();
    }
}
