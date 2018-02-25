package fga.evo.model.physics;

public class NewtonianBodyEnvironment {
    private double netForceX;
    private double netForceY;

    public double getCenterX() {
        return -1;
    }

    public double getCenterY() {
        return -1;
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
}