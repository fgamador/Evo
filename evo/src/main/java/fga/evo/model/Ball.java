package fga.evo.model;

import java.util.HashSet;
import java.util.Set;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, which could be Needless Complexity...
 */
public abstract class Ball {
    private static double speedLimit = 4;

    private double centerX;
    private double centerY;
    private double velocityX, velocityY;
    private double netForceX, netForceY;
    private Set<Ball> bondedBalls = new HashSet<>();

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
        capSpeed();
        updatePosition(subticksPerTick);
        clearForces();
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

    private void capSpeed() {
        // TODO simpler check before doing this one? e.g. abs(vx) + abs(vy) > max/2?
        // numerical/discretization problems can cause extreme velocities; cap them
        double speedSquared = sqr(velocityX) + sqr(velocityY);
        if (speedSquared > sqr(speedLimit)) {
            double throttling = speedLimit / Math.sqrt(speedSquared);
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

    public abstract double getMass();

    public abstract double getRadius();

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
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getSpeedLimit() {
        return speedLimit;
    }

    public static void setSpeedLimit(double val) {
        speedLimit = val;
    }
}
