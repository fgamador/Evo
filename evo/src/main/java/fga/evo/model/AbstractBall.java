package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Factored out of Cell as a base class
 * to honor the Single Responsibility Principle, but could be Needless Complexity...
 */
public abstract class AbstractBall {
    private static double speedLimit = 4;
    private static double overlapForceFactor = 1;

    private double centerX;
    private double centerY;
    private double velocityX, velocityY;
    private double netForceX, netForceY;

    /**
     * Sets the ball's initial position. All subsequent updates to position should be done by {@link #move()}.
     */
    public void setCenterPosition(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the ball's initial velocity. All subsequent updates to velocity should be done by {@link #move()}.
     */
    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the ball that will be used by the next call to {@link #move()}. This is the only way to
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
     */
    void move() {
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = netForceX / getMass();
        final double accelerationY = netForceY / getMass();

        // the velocity during this time interval
        velocityX += accelerationX;
        velocityY += accelerationY;

        // TODO simpler check before doing this one? e.g. abs(vx) + abs(vy) > max/2?
        // numerical/discretization problems can cause extreme velocities; cap them
        final double speedSquared = sqr(velocityX) + sqr(velocityY);
        if (speedSquared > sqr(speedLimit)) {
            final double throttling = speedLimit / Math.sqrt(speedSquared);
            velocityX *= throttling;
            velocityY *= throttling;
        }

        // the position at the end of this time interval
        setCenterPosition(centerX + velocityX, centerY + velocityY);

        // clear the forces
        netForceX = netForceY = 0;
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

    /**
     * Returns the force exerted on the ball if it is in collision with a wall to its left (smaller x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    double calcMinXWallCollisionForce(final double wallX) {
        double overlap = getRadius() - (getCenterX() - wallX);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall to its right (larger x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    double calcMaxXWallCollisionForce(final double wallX) {
        double overlap = getCenterX() + getRadius() - wallX;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall below it (smaller y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    double calcMinYWallCollisionForce(final double wallY) {
        double overlap = getRadius() - (getCenterY() - wallY);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall above it (larger y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    double calcMaxYWallCollisionForce(final double wallY) {
        double overlap = getCenterY() + getRadius() - wallY;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    static double calcOverlapForce(final double overlap) {
        return overlapForceFactor * overlap;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getSpeedLimit() {
        return speedLimit;
    }

    public static void setSpeedLimit(final double val) {
        speedLimit = val;
    }

    public static double getOverlapForceFactor() {
        return overlapForceFactor;
    }

    public static void setOverlapForceFactor(final double val) {
        overlapForceFactor = val;
    }
}
