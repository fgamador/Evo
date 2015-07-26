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
    private static double speedLimit = 4;

    private Set<Cell> bondedCells = new HashSet<>();
    private double mass;
    private double radius;
    private double centerX, centerY;
    private double velocityX, velocityY;
    private double forceX, forceY;
    private int physics = 1;

    public Cell(final double mass, final double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public void addBond(Cell cell2) {
        bondedCells.add(cell2);
        cell2.bondedCells.add(this);
    }

    /**
     * Sets the cell's initial position. All subsequent updates to position should be done by {@link #move()}.
     */
    public final void setPosition(final double centerX, final double centerY) {
        assert centerX >= 0 && centerY >= 0;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the cell's initial velocity. All subsequent updates to velocity should be done by {@link #move()}.
     */
    public final void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the cell that will be used by the next call to {@link #move()}. This is the only way to
     * influence the cell's motion (after setting its initial position and possibly velocity).
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
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = forceX / mass;
        final double accelerationY = forceY / mass;

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
        centerX += velocityX;
        centerY += velocityY;

        // clear the forces
        forceX = forceY = 0;
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
     * Returns the force exerted on the cell if it is in collision with a wall above it (smaller y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcLowYWallCollisionForce(final double wallY) {
        final double overlap = radius - (centerY - wallY);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall below it (larger y position).
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
     * Updates the forces on both of the cells. Call this only once for any particular pair of cells.
     *
     * @param cell2 the other cell
     */
    public void addInterCellForces(final Cell cell2) {
        final double relativeCenterX = centerX - cell2.centerX;
        final double relativeCenterY = centerY - cell2.centerY;
        final double centerSeparation = Math.sqrt(sqr(relativeCenterX) + sqr(relativeCenterY));

        if (centerSeparation != 0) {
            if (bondedCells.contains(cell2)) {
                if (physics == 1) {
                    addBondForces(cell2, relativeCenterX, relativeCenterY, centerSeparation);
                } else {
                    addBondForces2(cell2, relativeCenterX, relativeCenterY, centerSeparation);
                }
            } else {
                addCollisionForces(cell2, relativeCenterX, relativeCenterY, centerSeparation);
            }
        }
    }

    /**
     * Adds forces to the cells that will move them back toward just touching one another.
     */
    private void addBondForces(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
        final double overlap = radius + cell2.radius - centerSeparation;
        addOverlapForces(cell2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
    }

    /**
     * Experimental. Adds forces to the cells that, in the absence of other forces, will restore the
     * gap/overlap to zero on the next call to {@link #move()}.
     */
    private void addBondForces2(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
        final double relativeVelocityX = velocityX - cell2.velocityX;
        final double relativeVelocityY = velocityY - cell2.velocityY;
        final double compressionFactor = ((radius + cell2.radius) / centerSeparation) - 1;
        final double massFactor = (1 / mass) + (1 / cell2.mass);
        final double forceX = ((compressionFactor * relativeCenterX) - relativeVelocityX) / massFactor;
        final double forceY = ((compressionFactor * relativeCenterY) - relativeVelocityY) / massFactor;
        addForce(forceX, forceY);
        cell2.addForce(-forceX, -forceY);
    }

    /**
     * Adds forces to the cells that will push them away from one another.
     */
    private void addCollisionForces(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
        final double overlap = radius + cell2.radius - centerSeparation;
        if (overlap > 0) {
            addOverlapForces(cell2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
        }
    }

    private void addOverlapForces(Cell cell2, double relativeCenterX, double relativeCenterY, double centerSeparation, double overlap) {
        final double force = Cell.calcOverlapForce(overlap);
        final double forceX = (relativeCenterX / centerSeparation) * force;
        final double forceY = (relativeCenterY / centerSeparation) * force;
        addForce(forceX, forceY);
        cell2.addForce(-forceX, -forceY);
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

    public final int getPhysics() {
        return physics;
    }

    public final void setPhysics(final int val) {
        physics = val;
    }

    public static double sqr(double val) {
        return val * val;
    }

    public static double calcOverlapForce(final double overlap) {
        return overlapForceFactor * overlap;
    }

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
