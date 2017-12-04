package fga.evo.model;

import java.util.HashSet;
import java.util.Set;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, which could be Needless Complexity...
 */
public class Ball {
    static DoubleParameter speedLimit = new DoubleParameter(4);
    static DoubleParameter overlapForceFactor = new DoubleParameter(1);
    static DoubleParameter overlapAccumulatorRetentionRate = new DoubleParameter(0.95);

    private double radius;
    private double area;
    private double mass;
    private double centerX;
    private double centerY;
    private double velocityX, velocityY;
    private double netForceX, netForceY;
    private Set<Ball> bondedBalls = new HashSet<>();
    private DecayingAccumulator overlapAccumulator = new DecayingAccumulator(overlapAccumulatorRetentionRate);

    /**
     * Sets the ball's initial position. All subsequent updates to position should be done by {@link #subtickPhysics(int)}.
     */
    public void setCenterPosition(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the ball's initial velocity. All subsequent updates to velocity should be done by {@link #subtickPhysics(int)}.
     */
    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the ball that will be used by the next call to {@link #subtickPhysics(int)}. This is the only way to
     * influence the ball's motion (after setting its initial position and possibly velocity).
     *
     * @param forceX X-component of the force
     * @param forceY Y-component of the force
     */
    void addForce(double forceX, double forceY) {
        netForceX += forceX;
        netForceY += forceY;
    }

    /**
     * Updates the ball's velocity and position per the forces currently on it, then clears the forces.
     *
     * @param subticksPerTick time resolution
     */
    void subtickPhysics(int subticksPerTick) {
        updateVelocity(subticksPerTick);
        limitSpeed();
        updatePosition(subticksPerTick);
        clearForces();
        overlapAccumulator.decay();
    }

    private void updateVelocity(int subticksPerTick) {
        assert getMass() > 0;

        // the acceleration to apply instantaneously at the beginning of this subtick
        double accelerationX = netForceX / getMass();
        double accelerationY = netForceY / getMass();

        // the velocity during this subtick
        velocityX += accelerationX / subticksPerTick;
        velocityY += accelerationY / subticksPerTick;
    }

    // numerical/discretization problems can cause extreme velocities; cap them
    private void limitSpeed() {
        double speedSquared = sqr(velocityX) + sqr(velocityY);
        double maxSpeed = speedLimit.getValue();
        if (speedSquared > sqr(maxSpeed)) {
            double throttling = maxSpeed / Math.sqrt(speedSquared);
            velocityX *= throttling;
            velocityY *= throttling;
        }
    }

    private void updatePosition(int subticksPerTick) {
        // the position at the end of this subtick
        setCenterPosition(centerX + (velocityX / subticksPerTick), centerY + (velocityY / subticksPerTick));
    }

    private void clearForces() {
        netForceX = netForceY = 0;
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

    public void setMass(double val) {
        mass = val;
    }

    public double getMass() {
        return mass;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getNetForceX() {
        return netForceX;
    }

    public double getNetForceY() {
        return netForceY;
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
