package fga.evo.model.geometry;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OverlappableCirclesBubbleSortedByMinXTest {
    @Test
    public void threeCirclesAddInSortedOrder() {
        OverlappableCirclesBubbleSortedByMinX circles = new OverlappableCirclesBubbleSortedByMinX();
        OverlappableCircle circle1 = createCircle(1, 1, 0);
        OverlappableCircle circle2 = createCircle(1, 2, 0);
        OverlappableCircle circle3 = createCircle(1, 3, 0);

        circles.addAll(Arrays.asList(circle3, circle2, circle1));

        assertEquals(3, circles.size());
        assertEquals(circle1, circles.get(0));
        assertEquals(circle2, circles.get(1));
        assertEquals(circle3, circles.get(2));
    }

    private OverlappableCircle createCircle(int radius, int centerX, int centerY) {
        return new OverlappableCircle() {
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

            @Override
            public void onOverlap(OverlappableCircle circle, double overlap) {
            }
        };
    }
}