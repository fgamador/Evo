package fga.evo.model;

/**
 * Collision and bond forces for balls.
 */
public class InteractionForces {
    static double overlapForceFactor = 1;

    /**
     * Returns the force exerted on the ball if it is in collision with a wall to its left (smaller x position).
     *
     * @param ball  the ball
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    static double calcMinXWallCollisionForce(Ball ball, double wallX) {
        double overlap = ball.getRadius() - (ball.getCenterX() - wallX);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall to its right (larger x position).
     *
     * @param ball  the ball
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    static double calcMaxXWallCollisionForce(Ball ball, double wallX) {
        double overlap = ball.getCenterX() + ball.getRadius() - wallX;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall below it (smaller y position).
     *
     * @param ball  the ball
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    static double calcMinYWallCollisionForce(Ball ball, double wallY) {
        double overlap = ball.getRadius() - (ball.getCenterY() - wallY);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the ball if it is in collision with a wall above it (larger y position).
     *
     * @param ball  the ball
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    static double calcMaxYWallCollisionForce(Ball ball, double wallY) {
        double overlap = ball.getCenterY() + ball.getRadius() - wallY;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    static double calcOverlapForce(final double overlap) {
        return overlapForceFactor * overlap;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getOverlapForceFactor() {
        return overlapForceFactor;
    }

    public static void setOverlapForceFactor(final double val) {
        overlapForceFactor = val;
    }
}
