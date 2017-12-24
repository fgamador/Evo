package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.Circle;

import java.util.ArrayList;
import java.util.List;

class OverlapDetection {
    private List<Circle> circles = new ArrayList<>();

    public void addCircle(Circle circle) {
        circles.add(circle);
    }

    public void addCircles(List<? extends Circle> circles) {
        this.circles.addAll(circles);
    }

    public void clearCircles() {
        circles.clear();
    }

    public void notifyOverlaps() {
        // TODO Idea: keep balls sorted by centerX. Need check a ball against
        // only those others with greater indexes until we find another ball
        // whose centerX is beyond the max radius plus the first ball's
        // radius. Works for finding shadowing, too.
        for (int i = 0; i < circles.size(); i++) {
            Circle circle1 = circles.get(i);
            for (int j = i + 1; j < circles.size(); j++) {
                Circle circle2 = circles.get(j);
                circle1.onPossibleOverlap(circle2);
            }
        }
    }
}
