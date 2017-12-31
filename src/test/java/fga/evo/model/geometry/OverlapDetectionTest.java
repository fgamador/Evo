package fga.evo.model.geometry;

import fga.evo.model.Assert;
import org.junit.Test;

import java.util.*;

import static fga.evo.model.EvoTest.SQRT_2;
import static org.junit.Assert.*;

public class OverlapDetectionTest {
    private Map<Circle, Set<Circle>> overlaps = new HashMap<>();

    @Test
    public void notTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 3, -3);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(overlaps.containsKey(circle1));
        assertFalse(overlaps.containsKey(circle2));
    }

    @Test
    public void justTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 2, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(overlaps.containsKey(circle1));
        assertFalse(overlaps.containsKey(circle2));
    }

    @Test
    public void tinyXOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1.99, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertOverlap(circle1, circle2);
    }

    @Test
    public void xOverlapWithoutYOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1, 2);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(overlaps.containsKey(circle1));
        assertFalse(overlaps.containsKey(circle2));
    }

    @Test
    public void xAndYOverlapWithoutCircleOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1.5, 1.5);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertFalse(overlaps.containsKey(circle1));
        assertFalse(overlaps.containsKey(circle2));
    }

//    @Test
//    public void detectsOverlapDespiteAddOrder() {
//        SpyCircle circle1 = new SpyCircle(1, 0, 0);
//        SpyCircle circle2 = new SpyCircle(1, 1.5, 0);
//        SpyCircle circle3 = new SpyCircle(1, 3, 0);
//
//        OverlapDetection testSubject = new OverlapDetection();
//        testSubject.addCircles(Arrays.asList(circle1, circle3, circle2));
//        testSubject.findAndNotifyOverlaps();
//
//        assertEquals(circle2, circle1.lastOverlapCircle);
//        assertEquals(circle3, circle2.lastOverlapCircle);
//        assertNull(circle3.lastOverlapCircle);
//    }

    @Test
    public void notificationIncludesOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1.5, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEquals(0.5, circle1.lastOverlap, 0);
        assertEquals(-1, circle2.lastOverlap, 0);
    }

    @Test
    public void notificationCanCalculateDiagonalOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1 / SQRT_2, -1 / SQRT_2);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEquals(1, circle1.lastOverlap, Assert.DEFAULT_DELTA);
        assertEquals(-1, circle2.lastOverlap, 0);
    }

    private void recordOverlap(OverlapDetection.Circle circle1, OverlapDetection.Circle circle2) {
        Set<Circle> overlapCircles = overlaps.computeIfAbsent(circle1, k -> new HashSet<>());
        overlapCircles.add(circle2);
    }

    private void assertOverlap(SpyCircle circle1, SpyCircle circle2) {
        assertTrue(overlaps.get(circle1).contains(circle2));
        assertTrue(overlaps.get(circle2).contains(circle1));
    }

    private class SpyCircle implements OverlapDetection.Circle {
        private double radius;
        private double centerX;
        private double centerY;
        double lastOverlap = -1;

        SpyCircle(double radius, double centerX, double centerY) {
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

        @Override
        public void onOverlap(OverlapDetection.Circle circle, double overlap) {
            recordOverlap(this, circle);
            recordOverlap(circle, this);

            lastOverlap = overlap;
        }
    }
}