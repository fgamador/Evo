package fga.evo.model.geometry;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OverlappableCirclesBubbleSortedByMinXTest {
    @Test
    public void addAddsInSortedOrder() {
        OverlappableCirclesBubbleSortedByMinX testSubject = new OverlappableCirclesBubbleSortedByMinX();
        OverlappableCircle circle1 = createCircle(1, 1, 0);
        OverlappableCircle circle2 = createCircle(1, 2, 0);

        testSubject.add(circle2);
        testSubject.add(circle1);

        assertEquals(2, testSubject.size());
        assertEquals(circle1, testSubject.get(0));
        assertEquals(circle2, testSubject.get(1));
    }

    @Test
    public void addAllAddsInSortedOrder() {
        OverlappableCirclesBubbleSortedByMinX testSubject = new OverlappableCirclesBubbleSortedByMinX();
        OverlappableCircle circle1 = createCircle(1, 1, 0);
        OverlappableCircle circle2 = createCircle(1, 2, 0);
        OverlappableCircle circle3 = createCircle(1, 3, 0);

        testSubject.addAll(Arrays.asList(circle3, circle2, circle1));

        assertEquals(3, testSubject.size());
        assertEquals(circle1, testSubject.get(0));
        assertEquals(circle2, testSubject.get(1));
        assertEquals(circle3, testSubject.get(2));
    }

    @Test
    public void sortByMinXSortsAfterMovement() {
        OverlappableCirclesBubbleSortedByMinX testSubject = new OverlappableCirclesBubbleSortedByMinX();
        MovableCircle circle1 = new MovableCircle(1, 3, 0);
        MovableCircle circle2 = new MovableCircle(1, 2, 0);
        MovableCircle circle3 = new MovableCircle(1, 1, 0);
        testSubject.addAll(Arrays.asList(circle3, circle2, circle1));

        circle1.setCenterX(1);
        circle2.setCenterX(2);
        circle3.setCenterX(3);

        testSubject.sortByMinX();

        assertEquals(3, testSubject.size());
        assertEquals(circle1, testSubject.get(0));
        assertEquals(circle2, testSubject.get(1));
        assertEquals(circle3, testSubject.get(2));
    }

    private OverlappableCircle createCircle(int radius, int centerX, int centerY) {
        return new MovableCircle(radius, centerX, centerY);
    }

    private static class MovableCircle implements OverlappableCircle {
        private double radius;
        private double centerX;
        private double centerY;

        MovableCircle(double radius, double centerX, double centerY) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override
        public double getRadius() {
            return radius;
        }

        @Override
        public double getCenterX() {
            return centerX;
        }

        @Override
        public double getCenterY() {
            return centerY;
        }

        void setCenterX(double val) {
            centerX = val;
        }

        @Override
        public void onOverlap(OverlappableCircle circle, double overlap) {
        }
    }
}