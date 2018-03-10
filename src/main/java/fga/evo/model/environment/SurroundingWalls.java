package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;
import fga.evo.model.physics.NewtonianBodyEnvironment;

/**
 * The four walls surrounding the cells.
 */
public class SurroundingWalls extends EnvironmentalInfluence {
    private double minX, maxX, minY, maxY;

    public SurroundingWalls(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public void updateEnvironment(Cell cell) {
        addLeftBarrierCollisionForce(cell.getEnvironment(), cell, minX);
        addRightBarrierCollisionForce(cell.getEnvironment(), cell, maxX);
        addLowBarrierCollisionForce(cell.getEnvironment(), cell, minY);
        addHighBarrierCollisionForce(cell.getEnvironment(), cell, maxY);
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its left (smaller x position).
     *  @param ball
     * @param wallX x-position of the barrier
     */
    public static void addLeftBarrierCollisionForce(NewtonianBodyEnvironment environment, Ball ball, double wallX) {
        double overlap = wallX - ball.getMinX();
        if (overlap > 0) {
            ball.recordOverlap(overlap);
            environment.addForce(Ball.calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its right (larger x position).
     *  @param ball
     * @param wallX x-position of the barrier
     */
    public static void addRightBarrierCollisionForce(NewtonianBodyEnvironment environment, Ball ball, double wallX) {
        double overlap = ball.getMaxX() - wallX;
        if (overlap > 0) {
            ball.recordOverlap(overlap);
            environment.addForce(-Ball.calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier below it (smaller y position).
     *  @param ball
     * @param wallY y-position of the barrier
     */
    public static void addLowBarrierCollisionForce(NewtonianBodyEnvironment environment, Ball ball, double wallY) {
        double overlap = wallY - ball.getMinY();
        if (overlap > 0) {
            ball.recordOverlap(overlap);
            environment.addForce(0, Ball.calcOverlapForce(overlap));
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier above it (larger y position).
     *  @param ball
     * @param wallY y-position of the barrier
     */
    public static void addHighBarrierCollisionForce(NewtonianBodyEnvironment environment, Ball ball, double wallY) {
        double overlap = ball.getMaxY() - wallY;
        if (overlap > 0) {
            ball.recordOverlap(overlap);
            environment.addForce(0, -Ball.calcOverlapForce(overlap));
        }
    }

    public void resizeWidth(double newWidth) {
        maxX = minX + newWidth;
    }

    public void resizeHeight(double newHeight) {
        minY = maxY - newHeight;
    }
}
