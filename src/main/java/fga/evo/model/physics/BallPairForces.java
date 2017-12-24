package fga.evo.model.physics;

import fga.evo.model.DoubleParameter;
import fga.evo.model.OverlapDetection;

import static fga.evo.model.Util.sqr;

/**
 * Collision and bond forces for pairs of balls. Split out from Ball in honor of the Single Responsibility Principle.
 */
public class BallPairForces {
    public static DoubleParameter dampingForceFactor = new DoubleParameter(1);

    /**
     * Adds forces to the balls that will move them back toward just touching one another.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    public static void addBondForces(Ball ball1, Ball ball2) {
        double centerSeparationSquared = calcCenterSeparationSquared(ball1, ball2);
        if (centerSeparationSquared == 0) {
            return;
        }

        double centerSeparation = Math.sqrt(centerSeparationSquared);
        if (ballsOverlap(ball1, ball2, centerSeparationSquared)) {
            notifyOverlap(ball1, ball2, centerSeparation);
        }
        addOverlapForces(ball1, ball2, centerSeparation);
        addDampingForces(ball1, ball2);
    }

    /**
     * Adds forces to the balls that will push them away from one another.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    public static void addCollisionForces(Ball ball1, Ball ball2) {
        double centerSeparationSquared = calcCenterSeparationSquared(ball1, ball2);
        if (centerSeparationSquared == 0) {
            return;
        }
        if (!ballsOverlap(ball1, ball2, centerSeparationSquared)) {
            return;
        }
        double centerSeparation = Math.sqrt(centerSeparationSquared);
        notifyOverlap(ball1, ball2, centerSeparation);
        addOverlapForces(ball1, ball2, centerSeparation);
    }

    private static void notifyOverlap(Ball ball1, Ball ball2, double centerSeparation) {
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        ball1.onOverlap(overlap);
        ball2.onOverlap(overlap);
    }

    private static boolean ballsOverlap(OverlapDetection.Circle ball1, OverlapDetection.Circle ball2, double centerSeparationSquared) {
        return sqr(ball1.getRadius() + ball2.getRadius()) > centerSeparationSquared;
    }

    private static double calcCenterSeparationSquared(Ball ball1, Ball ball2) {
        double ball1RelativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double ball1RelativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        return sqr(ball1RelativeCenterX) + sqr(ball1RelativeCenterY);
    }

    private static void addOverlapForces(Ball ball1, Ball ball2, double centerSeparation) {
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        double force = Ball.calcOverlapForce(overlap);
        double ball1RelativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double ball1RelativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        double forceX = (ball1RelativeCenterX / centerSeparation) * force;
        double forceY = (ball1RelativeCenterY / centerSeparation) * force;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }

    private static void addDampingForces(Ball ball1, Ball ball2) {
        double ball1RelativeVelocityX = ball1.getVelocityX() - ball2.getVelocityX();
        double ball1RelativeVelocityY = ball1.getVelocityY() - ball2.getVelocityY();
        double forceX = -dampingForceFactor.getValue() * ball1RelativeVelocityX;
        double forceY = -dampingForceFactor.getValue() * ball1RelativeVelocityY;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }
}
