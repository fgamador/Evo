package fga.evo.model.physics;

public class PairBond {
    private final NewtonianBody body1;
    private final NewtonianBody body2;
//    private double restLength = 0;

    public PairBond(NewtonianBody body1, NewtonianBody body2) {
        if (body1 == null || body2 == null)
            throw new IllegalArgumentException("Cannot bond null");
        if (body1 == body2)
            throw new IllegalArgumentException("Cannot bond a body to itself");

        this.body1 = body1;
        this.body2 = body2;
    }

//    public void setRestLength(double val) {
//        restLength = val;
//    }

    public boolean bondsTo(NewtonianBody body) {
        return body1 == body || body2 == body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof PairBond))
            return false;

        PairBond bond = (PairBond) obj;
        return (bond.body1 == body1 && bond.body2 == body2) || (bond.body1 == body2 && bond.body2 == body1);
    }

    @Override
    public int hashCode() {
        // Same algorithm as in java.util.Set#hashCode.
        return body1.hashCode() + body2.hashCode();
    }
}
