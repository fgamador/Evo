package fga.evo.model.physics;

import fga.evo.model.util.DecayingAccumulator;
import fga.evo.model.util.DoubleParameter;
import fga.evo.model.geometry.OverlappableCircle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static fga.evo.model.util.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, which could be Needless Complexity...
 */
public abstract class Ball extends NewtonianBody implements OverlappableCircle {
    public static DoubleParameter overlapForceFactor = new DoubleParameter(1);

    private double radius;
    private double area;
    private List<PairBond> bonds = new ArrayList<>();

    public abstract BallEnvironment getEnvironment();

    public void setRadius(double val) {
        assert val >= 0;
        radius = val;
        area = Math.PI * sqr(radius);
    }

    public PairBond addBond(Ball ball) {
        if (isBondedTo(ball))
            throw new IllegalStateException("Cannot bond the same ball twice");

        PairBond bond = new PairBond(this, ball);
        bonds.add(bond);
        ball.bonds.add(bond);
        return bond;
    }

    public PairBond removeBond(Ball ball) {
        Predicate<PairBond> bondsToBall = bond -> bond.bondsTo(ball);
        PairBond removed = bonds.stream().filter(bondsToBall).findFirst().orElse(null);
        bonds.removeIf(bondsToBall);
        ball.bonds.removeIf(bond -> bond.bondsTo(this));
        return removed;
    }

    public boolean isBondedTo(OverlappableCircle ball) {
        return bonds.stream().anyMatch(bond -> bond.bondsTo(ball));
    }

    /**
     * Updates the ball's velocity and position per the forces currently on it, then clears the forces.
     *
     * @param movesPerTick time resolution
     */
    public void move(int movesPerTick) {
        subtick(movesPerTick);
    }

    @Override
    public void onOverlap(OverlappableCircle circle, double overlap) {
        Ball ball2 = (Ball) circle;
        getEnvironment().addOverlap(overlap);
        ball2.getEnvironment().addOverlap(overlap);
        if (!isBondedTo(circle)) {
            PairCollision.addForces(this, ball2, overlap);
        }
    }

    public double calcElasticDeformationForce(double overlap) {
        return overlapForceFactor.getValue() * overlap;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public double getMinX() {
        return getCenterX() - getRadius();
    }

    public double getMaxX() {
        return getCenterX() + getRadius();
    }

    public double getMinY() {
        return getCenterY() - getRadius();
    }

    public double getMaxY() {
        return getCenterY() + getRadius();
    }

    public double getArea() {
        return area;
    }
}
