package fga.evo.model.geometry;

import fga.evo.model.Assert;
import org.junit.Test;

import java.util.*;

import static fga.evo.model.EvoTest.SQRT_2;
import static org.junit.Assert.*;

public class OverlapDetectionTest {
    private Map<Set<Circle>, Double> overlaps = new HashMap<>();

    @Test
    public void notTouchingNoOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 3, -3);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(foundOverlap(circle1, circle2));
    }

    @Test
    public void justTouchingNoOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 2, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(foundOverlap(circle1, circle2));
    }

    @Test
    public void tinyXOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1.99, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertTrue(foundOverlap(circle1, circle2));
    }

    @Test
    public void xOverlapWithoutYOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1, 2);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(foundOverlap(circle1, circle2));
    }

    @Test
    public void xAndYOverlapWithoutCircleOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1.5, 1.5);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(foundOverlap(circle1, circle2));
    }

    @Test
    public void detectsOverlapDespiteAddOrder() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1.5, 0);
        OverlappableCircle circle3 = createCircle(1, 3, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle3, circle2));
        testSubject.findAndNotifyOverlaps();

        assertTrue(foundOverlap(circle1, circle2));
        assertTrue(foundOverlap(circle2, circle3));
        assertFalse(foundOverlap(circle1, circle3));
    }

    @Test
    public void notificationIncludesOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1.5, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEquals(0.5, getOverlap(circle1, circle2), Assert.DEFAULT_DELTA);
    }

    @Test
    public void notificationCanCalculateDiagonalOverlap() {
        OverlappableCircle circle1 = createCircle(1, 0, 0);
        OverlappableCircle circle2 = createCircle(1, 1 / SQRT_2, -1 / SQRT_2);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEquals(1, getOverlap(circle1, circle2), Assert.DEFAULT_DELTA);
    }

    private void recordOverlap(Circle circle1, Circle circle2, double overlap) {
        Set<Circle> key = new HashSet<>(Arrays.asList(circle1, circle2));
        overlaps.put(key, overlap);
    }

    private boolean foundOverlap(Circle circle1, Circle circle2) {
        Set<Circle> key = new HashSet<>(Arrays.asList(circle1, circle2));
        return overlaps.containsKey(key);
    }

    private double getOverlap(Circle circle1, Circle circle2) {
        Set<Circle> key = new HashSet<>(Arrays.asList(circle1, circle2));
        return overlaps.get(key);
    }

    private OverlappableCircle createCircle(double radius, double centerX, double centerY) {
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
                assertFalse(foundOverlap(this, circle));
                recordOverlap(this, circle, overlap);
            }
        };
    }
}