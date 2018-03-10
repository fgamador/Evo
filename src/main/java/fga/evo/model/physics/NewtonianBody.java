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
    private final NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();

    public NewtonianBody() {
    }

    public NewtonianBody(double centerX, double centerY, double velocityX, double velocityY) {
        setCenterPosition(centerX, centerY);
        setVelocity(velocityX, velocityY);
    }

    public NewtonianBodyEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Sets the ball's initial position. All subsequent updates to position should be done by {@link #subtick}.
     */
    public void setCenterPosition(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the ball's initial velocity. All subsequent updates to velocity should be done by {@link #subtick}.
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void subtick(int subticksPerTick) {
        subtick(getEnvironment(), subticksPerTick);
    }

    public void subtick(NewtonianBodyEnvironment environment, int subticksPerTick) {
        updateVelocity(environment, subticksPerTick);
        limitSpeed();
        updatePosition(subticksPerTick);
        environment.clearForces();
    }

    private void updateVelocity(NewtonianBodyEnvironment environment, int subticksPerTick) {
        assert getMass() > 0;

        // the acceleration to apply instantaneously at the beginning of this subtick
        double accelerationX = environment.getNetForceX() / getMass();
        double accelerationY = environment.getNetForceY() / getMass();

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
