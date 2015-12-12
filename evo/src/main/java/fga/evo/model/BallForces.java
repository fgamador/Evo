package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Collision and bond forces for balls.
 * TODO move these back into Ball?
 */
public class BallForces {
    private static double overlapForceFactor = 1;
    private static double dampingForceFactor = 1;

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its left (smaller x position).
     *
     * @param ball  the ball
     * @param wallX x-position of the barrier
     */
    static void addLeftBarrierCollisionForce(Ball ball, double wallX) {
        double overlap = ball.getRadius() - (ball.getCenterX() - wallX);
        if (overlap > 0) {
            ball.onOverlap(overlap);
            ball.addForce(calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier to its right (larger x position).
     *
     * @param ball  the ball
     * @param wallX x-position of the barrier
     */
    static void addRightBarrierCollisionForce(Ball ball, double wallX) {
        double overlap = ball.getCenterX() + ball.getRadius() - wallX;
        if (overlap > 0) {
            ball.onOverlap(overlap);
            ball.addForce(-calcOverlapForce(overlap), 0);
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier below it (smaller y position).
     *
     * @param ball  the ball
     * @param wallY y-position of the barrier
     */
    static void addLowBarrierCollisionForce(Ball ball, double wallY) {
        double overlap = ball.getRadius() - (ball.getCenterY() - wallY);
        if (overlap > 0) {
            ball.onOverlap(overlap);
            ball.addForce(0, calcOverlapForce(overlap));
        }
    }

    /**
     * Adds the force exerted on the ball if it is in collision with a barrier above it (larger y position).
     *
     * @param ball  the ball
     * @param wallY y-position of the barrier
     */
    static void addHighBarrierCollisionForce(Ball ball, double wallY) {
        double overlap = ball.getCenterY() + ball.getRadius() - wallY;
        if (overlap > 0) {
            ball.onOverlap(overlap);
            ball.addForce(0, -calcOverlapForce(overlap));
        }
    }

    /**
     * Adds the forces due to the interaction of one ball with another ball, such as a collision or a bond.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    static void addInterBallForces(Ball ball1, Ball ball2) {
        double centerSeparation = calcCenterSeparation(ball1, ball2);

        if (centerSeparation != 0) {
            if (ball1.isBondedTo(ball2)) {
                addBondForces(ball1, ball2, centerSeparation);
            } else {
                addCollisionForces(ball1, ball2, centerSeparation);
            }
        }
    }

    private static double calcCenterSeparation(Ball ball1, Ball ball2) {
        double relativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double relativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        return Math.sqrt(sqr(relativeCenterX) + sqr(relativeCenterY));
    }

    /**
     * Adds forces to the balls that will subtickPhysics them back toward just touching one another.
     */
    private static void addBondForces(Ball ball1, Ball ball2, double centerSeparation) {
        double overlap = calcAndRecordOverlap(ball1, ball2, centerSeparation);
        addOverlapForces(ball1, ball2, centerSeparation, overlap);
        addDampingForces(ball1, ball2);
    }

    /**
     * Adds forces to the balls that will push them away from one another.
     */
    private static void addCollisionForces(Ball ball1, Ball ball2, double centerSeparation) {
        double overlap = calcAndRecordOverlap(ball1, ball2, centerSeparation);
        if (overlap > 0) {
            addOverlapForces(ball1, ball2, centerSeparation, overlap);
        }
    }

    private static double calcAndRecordOverlap(Ball ball1, Ball ball2, double centerSeparation) {
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        if (overlap > 0) {
            ball1.onOverlap(overlap);
            ball2.onOverlap(overlap);
        }
        return overlap;
    }

    private static void addOverlapForces(Ball ball1, Ball ball2, double centerSeparation, double overlap) {
        double force = calcOverlapForce(overlap);
        double relativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double relativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        double forceX = (relativeCenterX / centerSeparation) * force;
        double forceY = (relativeCenterY / centerSeparation) * force;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }

    private static void addDampingForces(Ball ball1, Ball ball2) {
        double relativeVelocityX = ball1.getVelocityX() - ball2.getVelocityX();
        double relativeVelocityY = ball1.getVelocityY() - ball2.getVelocityY();
        double forceX = -dampingForceFactor * relativeVelocityX;
        double forceY = -dampingForceFactor * relativeVelocityY;
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

    public static void setOverlapForceFactor(double val) {
        overlapForceFactor = val;
    }

    public static double getDampingForceFactor() {
        return dampingForceFactor;
    }

    public static void setDampingForceFactor(double val) {
        dampingForceFactor = val;
    }
}
