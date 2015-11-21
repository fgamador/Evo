package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * A circular body subject to Newtonian motion physics. Base class for a Cell to honor the Separation of
 * Responsibilities Principle, but could be unnecessary complexity...
 */
public class Ball {
    private static double speedLimit = 4;

    private double mass;
    private double radius;
    private double area; // cached area derived from radius
    private double centerX;
    private double centerY;
    private double velocityX, velocityY;
    private double netForceX, netForceY;

    Ball(double radius) {
        setRadius(radius);
    }

    /**
     * Sets the cell's initial velocity. All subsequent updates to velocity should be done by {@link #move()}.
     */
    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the cell that will be used by the next call to {@link #move()}. This is the only way to
     * influence the cell's motion (after setting its initial position and possibly velocity).
     *
     * @param forceX X-component of the force
     * @param forceY Y-component of the force
     */
    void addForce(double forceX, double forceY) {
        netForceX += forceX;
        netForceY += forceY;
    }

    /**
     * Updates the cell's velocity and position per the forces currently on it, then clears the forces.
     */
    void move() {
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = netForceX / mass;
        final double accelerationY = netForceY / mass;

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

    public void setMass(double val) {
        mass = val;
    }

    public void setRadius(double val) {
        radius = val;
        area = Math.PI * sqr(radius);
    }

    public void setCenterPosition(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
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

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getSpeedLimit() {
        return speedLimit;
    }

    public static void setSpeedLimit(final double val) {
        speedLimit = val;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }
}