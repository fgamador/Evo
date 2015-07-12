package fga.evo.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The basic living unit in evo. A circular entity that can move and grow.
 *
 * @author Franz Amador
 */
public class Cell {
    private static double overlapForceFactor = 1;

    private Set<Cell> bondedCells = new HashSet<>();
    private double mass;
    private double radius;
    private double centerX, centerY;
    private double velocityX, velocityY;
    private double forceX, forceY;

    public Cell(final double mass, final double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public void addBond(Cell cell2) {
        bondedCells.add(cell2);
        cell2.bondedCells.add(this);
    }

    /**
     * Adds a force on the cell that will influence the next call to {@link #move()}.
     *
     * @param x X-component of the force
     * @param y Y-component of the force
     */
    public final void addForce(final double x, final double y) {
        forceX += x;
        forceY += y;
    }

    /**
     * Updates the cell's velocity and position per the forces currently on it, then clears the forces.
     */
    public final void move() {
        // the acceleration to apply continuously over this time interval
        double accelerationX = forceX / mass;
        double accelerationY = forceY / mass;
        // the position at the end of the time interval, updated by the average velocity
        centerX += velocityX + accelerationX / 2;
        centerY += velocityY + accelerationY / 2;
        // the velocity at the end of the time interval
        velocityX += accelerationX;
        velocityY += accelerationY;
        // clear the forces
        forceX = forceY = 0;
    }

    public final void setPosition(final double centerX, final double centerY) {
        assert centerX >= 0 && centerY >= 0;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its left (smaller x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcLowXWallCollisionForce(final double wallX) {
        final double overlap = radius - (centerX - wallX);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its right (larger x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcHighXWallCollisionForce(final double wallX) {
        final double overlap = centerX + radius - wallX;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its left (smaller y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcLowYWallCollisionForce(final double wallY) {
        final double overlap = radius - (centerY - wallY);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its right (larger y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcHighYWallCollisionForce(final double wallY) {
        final double overlap = centerY + radius - wallY;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Adds the forces due to the interaction of this cell with another cell, such as a collision or a bond.
     *
     * @param cell2 the other cell
     */
    public void addInterCellForces(final Cell cell2) {
        final double deltaX = cell2.getCenterX() - getCenterX();
        final double deltaY = cell2.getCenterY() - getCenterY();
        final double separation = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        final double overlap = getRadius() + cell2.getRadius() - separation;

        if ((overlap > 0 || bondedCells.contains(cell2)) && separation != 0) {
            final double force = Cell.calcOverlapForce(overlap);
            final double forceX = (deltaX / separation) * force;
            final double forceY = (deltaY / separation) * force;

            addForce(-forceX, -forceY);
            cell2.addForce(forceX, forceY);
        }
    }

    public final double getRadius() {
        return radius;
    }

    public final double getCenterX() {
        return centerX;
    }

    public final double getCenterY() {
        return centerY;
    }

    public final double getVelocityX() {
        return velocityX;
    }

    public final double getVelocityY() {
        return velocityY;
    }

    public final double getForceX() {
        return forceX;
    }

    public final double getForceY() {
        return forceY;
    }

    public static double calcOverlapForce(double overlap) {
        return overlapForceFactor * overlap;
    }

    public static double getOverlapForceFactor() {
        return overlapForceFactor;
    }

    public static void setOverlapForceFactor(final double val) {
        overlapForceFactor = val;
    }
}
