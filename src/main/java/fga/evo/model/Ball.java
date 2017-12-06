package fga.evo.model;

import fga.evo.model.physics.NewtonianBody;

import java.util.HashSet;
import java.util.Set;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, which could be Needless Complexity...
 */
public class Ball extends NewtonianBody {
    static DoubleParameter overlapForceFactor = new DoubleParameter(1);
    static DoubleParameter overlapAccumulatorRetentionRate = new DoubleParameter(0.95);

    private double radius;
    private double area;
    private Set<Ball> bondedBalls = new HashSet<>();
    private DecayingAccumulator overlapAccumulator = new DecayingAccumulator(overlapAccumulatorRetentionRate);

    /**
     * Updates the ball's velocity and position per the forces currently on it, then clears the forces.
     *
     * @param subticksPerTick time resolution
     */
    void subtickPhysics(int subticksPerTick) {
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
    /**
     * Adds the forces due to the interaction of the ball with another ball, such as a collision or a bond.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball another ball
     */
    public void addBallPairForces(Ball ball) {
        BallPairForces.addBallPairForces(this, ball);
    }

    public void addBond(Ball ball) {
        bondedBalls.add(ball);
        ball.bondedBalls.add(this);
    }

    public void removeBond(Ball ball) {
        bondedBalls.remove(ball);
        ball.bondedBalls.remove(this);
    }

    public boolean isBondedTo(Ball ball) {
        return bondedBalls.contains(ball);
    }

    public void setRadius(double val) {
        radius = val;
        area = Math.PI * sqr(radius);
    }

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
}
