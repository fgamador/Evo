package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Collision and bond forces for balls.
 */
public class BallForces {
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

    /**
     * Adds the forces due to the interaction of one ball with another ball, such as a collision or a bond.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    static void addInterBallForces(Ball ball1, Ball ball2) {
        double relativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double relativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        double centerSeparation = Math.sqrt(sqr(relativeCenterX) + sqr(relativeCenterY));

        if (centerSeparation != 0) {
            if (ball1.isBondedTo(ball2)) {
                addBondForces(ball1, ball2, relativeCenterX, relativeCenterY, centerSeparation);
            } else {
                addCollisionForces(ball1, ball2, relativeCenterX, relativeCenterY, centerSeparation);
            }
        }
    }

    /**
     * Adds forces to the balls that will move them back toward just touching one another.
     */
    private static void addBondForces(Ball ball1, Ball ball2, double relativeCenterX, double relativeCenterY, double centerSeparation) {
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        addOverlapForces(ball1, ball2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
    }

    /**
     * Adds forces to the balls that will push them away from one another.
     */
    private static void addCollisionForces(Ball ball1, Ball ball2, double relativeCenterX, double relativeCenterY, double centerSeparation) {
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        if (overlap > 0) {
            addOverlapForces(ball1, ball2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
        }
    }

    private static void addOverlapForces(Ball ball1, Ball ball2, double relativeCenterX, double relativeCenterY, double centerSeparation, double overlap) {
        double force = calcOverlapForce(overlap);
        double forceX = (relativeCenterX / centerSeparation) * force;
        double forceY = (relativeCenterY / centerSeparation) * force;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }

    static double calcOverlapForce(double overlap) {
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
