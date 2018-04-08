package fga.evo.model.geometry;

import java.util.List;

public class OverlapDetection {
    private CirclesBubbleSortedByMinX<OverlappableCircle> circles = new CirclesBubbleSortedByMinX<>();

    public void addCircle(OverlappableCircle circle) {
        circles.add(circle);
    }

    public void addCircles(List<? extends OverlappableCircle> circles) {
        this.circles.addAll(circles);
    }

    public void clear() {
        circles.clear();
    }

    public void findAndNotifyOverlaps() {
        circles.sortByMinX();
        for (int i = 0; i < circles.size(); ++i) {
            OverlappableCircle circle1 = circles.get(i);
            for (int j = i + 1; j < circles.size(); ++j) {
                OverlappableCircle circle2 = circles.get(j);
                if (Circles.minX(circle2) >= Circles.maxX(circle1))
                    break;

                double centerSeparationSquared = Circles.calcCenterSeparationSquared(circle1, circle2);
                if (Circles.circlesOverlap(circle1, circle2, centerSeparationSquared)) {
                    double overlap = Circles.toOverlap(circle1, circle2, Math.sqrt(centerSeparationSquared));
                    circle1.onOverlap(circle2, overlap);
                }
            }
        }
    }
}
