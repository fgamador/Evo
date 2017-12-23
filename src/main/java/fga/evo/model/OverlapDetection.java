package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.PairCollision;

import java.util.List;

class OverlapDetection {
    public void addCollisionForces(List<Cell> cells) {
        for (int i = 0; i < cells.size(); i++) {
            Ball ball = cells.get(i);

            // TODO Idea: keep cells sorted by centerX. Need check a ball against
            // only those others with greater indexes until we find another ball
            // whose centerX is beyond the max radius plus the first ball's
            // radius. Works for finding shadowing, too.
            for (int j = i + 1; j < cells.size(); j++) {
                Ball ball2 = cells.get(j);
                if (!ball.isBondedTo(ball2))
                    PairCollision.addForces(ball, ball2);
            }
        }
    }
}
