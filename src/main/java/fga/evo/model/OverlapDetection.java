package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.PairCollision;

import java.util.ArrayList;
import java.util.List;

class OverlapDetection {
    private List<Ball> balls = new ArrayList<>();

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    void addBalls(List<? extends Ball> balls) {
        this.balls.addAll(balls);
    }

    void clearBalls() {
        balls.clear();
    }

    void addCollisionForces() {
        // TODO Idea: keep balls sorted by centerX. Need check a ball against
        // only those others with greater indexes until we find another ball
        // whose centerX is beyond the max radius plus the first ball's
        // radius. Works for finding shadowing, too.
        for (int i = 0; i < balls.size(); i++) {
            Ball ball1 = balls.get(i);
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ball2 = balls.get(j);
                if (!ball1.isBondedTo(ball2))
                    onPossibleOverlap(ball1, ball2);
            }
        }
    }

    private void onPossibleOverlap(Ball ball1, Ball ball2) {
        PairCollision.addForces(ball1, ball2);
    }
}
