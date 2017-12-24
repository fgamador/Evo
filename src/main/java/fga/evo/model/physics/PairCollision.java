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
        if (Circles.circlesOverlapWithOffset(ball1, ball2)) {
            double centerSeparation = Math.sqrt(Circles.calcCenterSeparationSquared(ball1, ball2));
            BallPairForces.notifyOverlap(ball1, ball2, centerSeparation);
            BallPairForces.addOverlapForces(ball1, ball2, centerSeparation);
        }
    }
}
