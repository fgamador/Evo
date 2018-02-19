package fga.evo.model.physics;

public class NewtonianBodyForces {
    private double netForceX;
    private double netForceY;

    public NewtonianBodyForces() {
    }

    /**
     * Adds a force on the body that will be used by the next call to {@link NewtonianBody#subtick(int)}. This is the
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