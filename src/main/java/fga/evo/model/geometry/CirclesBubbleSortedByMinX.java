package fga.evo.model.geometry;

import java.util.ArrayList;
import java.util.List;

public class CirclesBubbleSortedByMinX<T extends Circle> {
    private List<T> circles = new ArrayList<>();

    public void add(T circle) {
        circles.add(circle);
        sortByMinX();
    }

    public void addAll(List<? extends T> circles) {
        this.circles.addAll(circles);
        sortByMinX();
    }

    public void clear() {
        circles.clear();
    }

    public T get(int index) {
        return circles.get(index);
    }

    public int size() {
        return circles.size();
    }

    public void sortByMinX() {
        boolean swapped;
        do {
            swapped = false;
            for (int i = circles.size() - 1; i > 0; --i) {
                if (Circles.minX(circles.get(i)) < Circles.minX(circles.get(i - 1))) {
                    swapCircles(i, i - 1);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    private void swapCircles(int index1, int index2) {
        T temp = circles.get(index1);
        circles.set(index1, circles.get(index2));
        circles.set(index2, temp);
    }
}
