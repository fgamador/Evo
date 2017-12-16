package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Collision and bond forces for pairs of balls. Split out from Ball in honor of the Single Responsibility Principle.
 */
public class BallPairForces {
    static DoubleParameter dampingForceFactor = new DoubleParameter(1);

    /**
     * Adds the forces due to the interaction of one ball with another ball, such as a collision or a bond.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    static void addBallPairForces(Ball ball1, Ball ball2) {
        if (!haveSameCenter(ball1, ball2)) {
            if (ball1.isBondedTo(ball2)) {
                addBondForces(ball1, ball2);
            } else {
                addCollisionForces(ball1, ball2);
            }
        }
    }

    private static boolean haveSameCenter(Ball ball1, Ball ball2) {
        return ball1.getCenterX() == ball2.getCenterX() && ball1.getCenterY() == ball2.getCenterY();
    }

    /**
     * Adds forces to the balls that will move them back toward just touching one another.
     */
    private static void addBondForces(Ball ball1, Ball ball2) {
        double centerSeparation = calcCenterSeparation(ball1, ball2);
        double overlap = calcAndRecordOverlap(ball1, ball2, centerSeparation);
        addOverlapForces(ball1, ball2, centerSeparation, overlap);
        addDampingForces(ball1, ball2);
    }

    /**
     * Adds forces to the balls that will push them away from one another.
     */
    private static void addCollisionForces(Ball ball1, Ball ball2) {
        double centerSeparation = calcCenterSeparation(ball1, ball2);
        double overlap = ball1.getRadius() + ball2.getRadius() - centerSeparation;
        if (overlap > 0) {
            ball1.onOverlap(overlap);
            ball2.onOverlap(overlap);
        }
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
        double force = Ball.calcOverlapForce(overlap);
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
        double forceX = -dampingForceFactor.getValue() * relativeVelocityX;
        double forceY = -dampingForceFactor.getValue() * relativeVelocityY;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }

    private static double calcCenterSeparation(Ball ball1, Ball ball2) {
        double centerSeparationSquared = calcCenterSeparationSquared(ball1, ball2);
        return Math.sqrt(centerSeparationSquared);
    }

    private static double calcCenterSeparationSquared(Ball ball1, Ball ball2) {
        double relativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double relativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        return sqr(relativeCenterX) + sqr(relativeCenterY);
    }
}
