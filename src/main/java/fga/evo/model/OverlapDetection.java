package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.PairCollision;

import java.util.ArrayList;
import java.util.List;

class OverlapDetection {
    static List<Ball> balls = new ArrayList<>();

    static void addBall(Ball ball) {
        balls.add(ball);
    }

    static void addBalls(List<Cell> balls) {
        OverlapDetection.balls.addAll(balls);
    }

    public void addCollisionForces(List<? extends Ball> balls) {
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
