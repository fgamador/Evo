package fga.evo.model.physics;

import fga.evo.model.DecayingAccumulator;
import fga.evo.model.DoubleParameter;
import fga.evo.model.geometry.OverlapDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, which could be Needless Complexity...
 */
public class Ball extends NewtonianBody implements OverlapDetection.Circle {
    public static DoubleParameter overlapForceFactor = new DoubleParameter(1);
    public static DoubleParameter overlapAccumulatorRetentionRate = new DoubleParameter(0.95);

    private double radius;
    private double area;
    private List<PairBond> bonds = new ArrayList<>();
    private DecayingAccumulator overlapAccumulator = new DecayingAccumulator(overlapAccumulatorRetentionRate);

    /**
     * Updates the ball's velocity and position per the forces currently on it, then clears the forces.
     *
     * @param subticksPerTick time resolution
     */
    public void subtickPhysics(int subticksPerTick) {
        subtick(subticksPerTick);
        overlapAccumulator.decay();
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its left (smaller x position).
     *
     * @param wallX x-position of the barrier
     */
    public void addLeftBarrierCollisionForce(double wallX) {
        double overlap = getRadius() - (getCenterX() - wallX);
        if (overlap > 0) {
            onOverlap(overlap);
            addForce(calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its right (larger x position).
     *
     * @param wallX x-position of the barrier
     */
    public void addRightBarrierCollisionForce(double wallX) {
        double overlap = getCenterX() + getRadius() - wallX;
        if (overlap > 0) {
            onOverlap(overlap);
            addForce(-calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier below it (smaller y position).
     *
     * @param wallY y-position of the barrier
     */
    public void addLowBarrierCollisionForce(double wallY) {
        double overlap = getRadius() - (getCenterY() - wallY);
        if (overlap > 0) {
            onOverlap(overlap);
            addForce(0, calcOverlapForce(overlap));
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier above it (larger y position).
     *
     * @param wallY y-position of the barrier
     */
    public void addHighBarrierCollisionForce(double wallY) {
        double overlap = getCenterY() + getRadius() - wallY;
        if (overlap > 0) {
            onOverlap(overlap);
            addForce(0, -calcOverlapForce(overlap));
        }
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

    public boolean isBondedTo(OverlapDetection.Circle ball) {
        return bonds.stream().anyMatch(bond -> bond.bondsTo(ball));
    }

    public void setRadius(double val) {
        radius = val;
        area = Math.PI * sqr(radius);
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
    }

    public void onOverlap(double overlap) {
        overlapAccumulator.addValue(overlap);
    }

    public double getRecentTotalOverlap() {
        return overlapAccumulator.getTotal();
    }

    static double calcOverlapForce(double overlap) {
        return overlapForceFactor.getValue() * overlap;
    }

    @Override
    public void onOverlap(OverlapDetection.Circle circle, double overlap) {
        if (!isBondedTo(circle))
            PairCollision.addForces(this, (Ball) circle);
    }
}
