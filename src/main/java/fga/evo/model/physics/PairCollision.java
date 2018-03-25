package fga.evo.model.physics;

import fga.evo.model.geometry.Circles;

public class PairCollision {
    /**
     * Adds forces to the balls that will push them away from one another.
     * Updates the forces on both of the balls. Call this only once for any particular pair of balls.
     *  @param ball1 a ball
     * @param ball2 another ball
     * @param overlap overlap distance along inter-radial axis
     */
    public static void addForces(Ball ball1, Ball ball2, double overlap) {
        double centerSeparation = Circles.toCenterSeparation(ball1, ball2, overlap);
        if (centerSeparation != 0) {
            BallPairForces.addOverlapForces(ball1, ball2, centerSeparation, overlap);
        }
    }
}
