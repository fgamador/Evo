package fga.evo.model.physics;

import fga.evo.model.util.DoubleParameter;

/**
 * Collision and bond forces for pairs of balls. Split out from Ball in honor of the Single Responsibility Principle.
 */
public class BallPairForces {
    public static DoubleParameter dampingForceFactor = new DoubleParameter(1);

    public static void addOverlapForces(Ball ball1, Ball ball2, double centerSeparation, double overlap) {
        double force = Ball.calcOverlapForce(overlap);
        double ball1RelativeCenterX = ball1.getCenterX() - ball2.getCenterX();
        double ball1RelativeCenterY = ball1.getCenterY() - ball2.getCenterY();
        double forceX = (ball1RelativeCenterX / centerSeparation) * force;
        double forceY = (ball1RelativeCenterY / centerSeparation) * force;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }

    public static void addDampingForces(Ball ball1, Ball ball2) {
        double ball1RelativeVelocityX = ball1.getVelocityX() - ball2.getVelocityX();
        double ball1RelativeVelocityY = ball1.getVelocityY() - ball2.getVelocityY();
        double forceX = -dampingForceFactor.getValue() * ball1RelativeVelocityX;
        double forceY = -dampingForceFactor.getValue() * ball1RelativeVelocityY;
        ball1.addForce(forceX, forceY);
        ball2.addForce(-forceX, -forceY);
    }
}
