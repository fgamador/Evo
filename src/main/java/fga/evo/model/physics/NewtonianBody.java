package fga.evo.model.physics;

import fga.evo.model.util.DoubleParameter;

import static fga.evo.model.util.Util.sqr;

public abstract class NewtonianBody {
    public static DoubleParameter speedLimit = new DoubleParameter(4);

    private double mass;
    private double centerX;
    private double centerY;
    private double velocityX;
    private double velocityY;
    private double netForceX;
    private double netForceY;

    public NewtonianBody() {
    }

    public NewtonianBody(double centerX, double centerY, double velocityX, double velocityY) {
        setCenterPosition(centerX, centerY);
        setVelocity(velocityX, velocityY);
    }

    public abstract NewtonianBodyEnvironment getEnvironment();

    /**
     * Sets the ball's initial position. All subsequent updates to position should be done by {@link #subtick}.
     */
    public void setCenterPosition(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the body's initial velocity. All subsequent updates to velocity should be done by {@link #subtick}.
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the body that will be used by the next call to {@link NewtonianBody#subtick}. This is the
     * only way to influence the body's motion (after setting its initial position and possibly velocity).
     *
     * @param forceX X-component of the force
     * @param forceY Y-component of the force
     */
    public void addForce(double forceX, double forceY) {
        netForceX += forceX;
        netForceY += forceY;
    }

    public void clearForces() {
        netForceX = netForceY = 0;
    }

    public double getNetForceX() {
        return netForceX;
    }

    public double getNetForceY() {
        return netForceY;
    }

    public void subtick(int subticksPerTick) {
        NewtonianBodyEnvironment environment = getEnvironment();
        updateVelocity(environment, subticksPerTick);
        limitSpeed();
        updatePosition(subticksPerTick);
        clearForces();
    }

    private void updateVelocity(NewtonianBodyEnvironment environment, int subticksPerTick) {
        assert mass > 0;

        // the acceleration to apply instantaneously at the beginning of this subtick
        double accelerationX = getNetForceX() / getMass();
        double accelerationY = getNetForceY() / getMass();

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
}
