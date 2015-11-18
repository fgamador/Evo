package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Forces and motion for a cell.
 */
public class CellPhysics {
    private Cell cell; // TODO CellPhysics.CellApi

    public CellPhysics(Cell cell) {
        this.cell = cell;
    }

    void setPosition(final double centerX, final double centerY) {
        assert centerX >= 0;
        cell.centerX = centerX;
        cell.centerY = centerY;
    }

    void setVelocity(double velocityX, double velocityY) {
        cell.velocityX = velocityX;
        cell.velocityY = velocityY;
    }

    void addForce(final double forceX, final double forceY) {
        cell.netForceX += forceX;
        cell.netForceY += forceY;
    }

    void move() {
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = cell.netForceX / cell.mass;
        final double accelerationY = cell.netForceY / cell.mass;

        // the velocity during this time interval
        cell.velocityX += accelerationX;
        cell.velocityY += accelerationY;

        // TODO simpler check before doing this one? e.g. abs(vx) + abs(vy) > max/2?
        // numerical/discretization problems can cause extreme velocities; cap them
        final double speedSquared = sqr(cell.velocityX) + sqr(cell.velocityY);
        if (speedSquared > sqr(Cell.speedLimit)) {
            final double throttling = Cell.speedLimit / Math.sqrt(speedSquared);
            cell.velocityX *= throttling;
            cell.velocityY *= throttling;
        }

        // the position at the end of this time interval
        cell.centerX += cell.velocityX;
        cell.centerY += cell.velocityY;

        // clear the forces
        cell.netForceX = cell.netForceY = 0;
    }
}
