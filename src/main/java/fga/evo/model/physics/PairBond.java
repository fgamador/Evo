package fga.evo.model.physics;

import fga.evo.model.Ball;

import java.util.Objects;

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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PairBond pairBond = (PairBond) o;
//        return Objects.equals(body1, pairBond.body1) &&
//                Objects.equals(body2, pairBond.body2);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(body1, body2);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PairBond))
            return false;

        PairBond bond = (PairBond) obj;
        return (bond.body1 == body1 && bond.body2 == body2) || (bond.body2 == body1 && bond.body1 == body2);
    }
}
