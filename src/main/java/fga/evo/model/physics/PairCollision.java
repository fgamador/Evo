package fga.evo.model.physics;

import fga.evo.model.geometry.Circles;

public class PairCollision {
    /**
     * Adds forces to the balls that will push them away from one another.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *
     * @param ball1 a ball
     * @param ball2 another ball
     */
    public static void addForces(Ball ball1, Ball ball2) {
        addForces(ball1, ball2, Math.sqrt(Circles.calcCenterSeparationSquared(ball1, ball2)));
    }

    public static void addForces(Ball ball1, Ball ball2, double centerSeparation) {
        if (centerSeparation != 0) {
            BallPairForces.notifyOverlap(ball1, ball2, centerSeparation);
            BallPairForces.addOverlapForces(ball1, ball2, centerSeparation);
        }
    }
}
