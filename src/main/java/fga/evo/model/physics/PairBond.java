package fga.evo.model.physics;

import fga.evo.model.Ball;

public class PairBond {
    private final NewtonianBody body1;
    private final NewtonianBody body2;
//    private double restLength = 0;

    public PairBond(NewtonianBody body1, NewtonianBody body2) {
        if (body1 == body2)
            throw new IllegalArgumentException("Cannot bond a body to itself");

        this.body1 = body1;
        this.body2 = body2;
    }

//    public void setRestLength(double val) {
//        restLength = val;
//    }

    public NewtonianBody getBody1() {
        return body1;
    }

    public NewtonianBody getBody2() {
        return body2;
    }

    public boolean bondsTo(NewtonianBody body) {
        return body1 == body || body2 == body;
    }
}
