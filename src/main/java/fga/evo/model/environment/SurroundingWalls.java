package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;

/**
 * The four walls surrounding the cells.
 */
public class SurroundingWalls implements EnvironmentalInfluence, ForceInfluence {
    private double minX, maxX, minY, maxY;

    public SurroundingWalls(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public void addForce(Cell cell) {
        addLeftBarrierCollisionForce(cell, minX);
        addRightBarrierCollisionForce(cell, maxX);
        addLowBarrierCollisionForce(cell, minY);
        addHighBarrierCollisionForce(cell, maxY);
    }

    public void updateEnvironment(Cell cell) {
        addForce(cell);
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its left (smaller x position).
     *
     * @param ball
     * @param wallX x-position of the barrier
     */
    public static void addLeftBarrierCollisionForce(Ball ball, double wallX) {
        double overlap = wallX - ball.getMinX();
        if (overlap > 0) {
            ball.getEnvironment().addOverlap(overlap);
            ball.addForce(ball.calcElasticDeformationForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its right (larger x position).
     *
     * @param ball
     * @param wallX x-position of the barrier
     */
    public static void addRightBarrierCollisionForce(Ball ball, double wallX) {
        double overlap = ball.getMaxX() - wallX;
        if (overlap > 0) {
            ball.getEnvironment().addOverlap(overlap);
            ball.addForce(-ball.calcElasticDeformationForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier below it (smaller y position).
     *
     * @param ball
     * @param wallY y-position of the barrier
     */
    public static void addLowBarrierCollisionForce(Ball ball, double wallY) {
        double overlap = wallY - ball.getMinY();
        if (overlap > 0) {
            ball.getEnvironment().addOverlap(overlap);
            ball.addForce(0, ball.calcElasticDeformationForce(overlap));
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier above it (larger y position).
     *
     * @param ball
     * @param wallY y-position of the barrier
     */
    public static void addHighBarrierCollisionForce(Ball ball, double wallY) {
        double overlap = ball.getMaxY() - wallY;
        if (overlap > 0) {
            ball.getEnvironment().addOverlap(overlap);
            ball.addForce(0, -ball.calcElasticDeformationForce(overlap));
        }
    }

    public void resizeWidth(double newWidth) {
        maxX = minX + newWidth;
    }

    public void resizeHeight(double newHeight) {
        minY = maxY - newHeight;
    }
}
