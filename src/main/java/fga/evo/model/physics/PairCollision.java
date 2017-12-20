package fga.evo.model.physics;

public class PairCollision {
    public static void addForces(Ball ball1, Ball ball2) {
        BallPairForces.addCollisionForces(ball1, ball2);
    }
}
