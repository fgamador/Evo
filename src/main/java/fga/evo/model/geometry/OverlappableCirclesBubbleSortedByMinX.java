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

//    private void bubbleSortCirclesByMinX() {
//        for (int i = circles.size() - 1; i > 0; --i) {
//            if (minX(circles.get(i)) < minX(circles.get(i - 1))) {
//                swapCircles(i, i - 1);
//            }
//        }
//    }
//
//    private double minX(OverlappableCircle circle) {
//        return circle.getCenterX() - circle.getRadius();
//    }
//
//    private void swapCircles(int index1, int index2) {
//        OverlappableCircle temp = circles.get(index1);
//        circles.set(index1, circles.get(index2));
//        circles.set(index2, temp);
//    }
}
