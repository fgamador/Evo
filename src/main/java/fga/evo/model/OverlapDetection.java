package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.PairCollision;

import java.util.ArrayList;
import java.util.List;

class OverlapDetection {
    private List<Ball> balls = new ArrayList<>();

    static void addBall(Ball ball, OverlapDetection overlapDetection) {
        overlapDetection.addBall(ball);
    }

    void addBall(Ball ball) {
        balls.add(ball);
    }

    static void addBalls(List<Cell> balls, OverlapDetection overlapDetection) {
        overlapDetection.addBalls(balls);
    }

    void addBalls(List<Cell> balls) {
        this.balls.addAll(balls);
    }

    static void clearBalls(OverlapDetection overlapDetection) {
        overlapDetection.clearBalls();
    }

    void clearBalls() {
        balls.clear();
    }

    static void addCollisionForces(OverlapDetection overlapDetection) {
        overlapDetection.addCollisionForces();
    }

    void addCollisionForces() {
        addCollisionForces(balls);
    }

    private void addCollisionForces(List<? extends Ball> balls) {
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);

            // TODO Idea: keep balls sorted by centerX. Need check a ball against
            // only those others with greater indexes until we find another ball
            // whose centerX is beyond the max radius plus the first ball's
            // radius. Works for finding shadowing, too.
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ball2 = balls.get(j);
                if (!ball.isBondedTo(ball2))
                    PairCollision.addForces(ball, ball2);
            }
        }
    }
}
