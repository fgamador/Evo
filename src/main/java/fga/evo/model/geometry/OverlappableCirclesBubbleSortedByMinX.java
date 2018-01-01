package fga.evo.model.geometry;

import java.util.ArrayList;
import java.util.List;

public class OverlappableCirclesBubbleSortedByMinX {
    private List<OverlappableCircle> circles = new ArrayList<>();

    public void add(OverlappableCircle circle) {
        circles.add(circle);
    }

    public void addAll(List<? extends OverlappableCircle> circles) {
        this.circles.addAll(circles);
    }

    public void clear() {
        circles.clear();
    }

    public OverlappableCircle get(int index) {
        return circles.get(index);
    }

    public int size() {
        return circles.size();
    }
}
