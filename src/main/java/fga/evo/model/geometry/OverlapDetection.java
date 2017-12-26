package fga.evo.model.geometry;

import java.util.ArrayList;
import java.util.List;

public class OverlapDetection {
    private List<Circle> circles = new ArrayList<>();

    public void addCircle(Circle circle) {
        circles.add(circle);
    }

    public void addCircles(List<? extends Circle> circles) {
        this.circles.addAll(circles);
    }

    public void clear() {
        circles.clear();
    }

    public void findAndNotifyOverlaps() {
        // TODO Idea: keep balls sorted by centerX. Need check a ball against
        // only those others with greater indexes until we find another ball
        // whose centerX is beyond the max radius plus the first ball's
        // radius. Works for finding shadowing, too.
        for (int i = 0; i < circles.size(); i++) {
            Circle circle1 = circles.get(i);
            for (int j = i + 1; j < circles.size(); j++) {
                Circle circle2 = circles.get(j);
                double centerSeparationSquared = Circles.calcCenterSeparationSquared(circle1, circle2);
                if (Circles.circlesOverlap(circle1, circle2, centerSeparationSquared))
                    circle1.onOverlap(circle2);
            }
        }
    }

    public interface Circle extends fga.evo.model.geometry.Circle {
        void onOverlap(Circle circle);
    }
}
