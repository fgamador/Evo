package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Forces and motion for a cell.
 */
public class CellPhysics {
    private static double speedLimit = 4;

    private CellApi cell;
    private double velocityX, velocityY;
    private double netForceX, netForceY;

    public CellPhysics(CellApi cell) {
        this.cell = cell;
    }

    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    void addForce(double forceX, double forceY) {
        netForceX += forceX;
        netForceY += forceY;
    }

    void move() {
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = netForceX / cell.getMass();
        final double accelerationY = netForceY / cell.getMass();

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
        cell.setPosition(cell.getCenterX() + velocityX, cell.getCenterY() + velocityY);

        // clear the forces
        netForceX = netForceY = 0;
    }

    final double getVelocityX() {
        return velocityX;
    }

    final double getVelocityY() {
        return velocityY;
    }

    final double getNetForceX() {
        return netForceX;
    }

    final double getNetForceY() {
        return netForceY;
    }

    interface CellApi {
        double getMass();

        double getCenterX();

        double getCenterY();

        void setPosition(double centerX, double centerY);
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
}
