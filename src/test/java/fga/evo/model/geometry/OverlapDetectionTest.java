package fga.evo.model.geometry;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OverlapDetectionTest {
    @Test
    public void notTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 3, -3);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertNull(circle1.lastOverlapCircle);
        assertNull(circle2.lastOverlapCircle);
    }

    @Test
    public void justTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 2, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertNull(circle1.lastOverlapCircle);
        assertNull(circle2.lastOverlapCircle);
    }

    @Test
    public void slightXOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1.99, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEquals(circle2, circle1.lastOverlapCircle);
        assertNull(circle2.lastOverlapCircle);
    }

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
    public void xOverlapWithoutYOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1, 2);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertNull(circle1.lastOverlapCircle);
        assertNull(circle2.lastOverlapCircle);
    }

    @Test
    public void xAndYOverlapWithoutCircleOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 1.5, 1.5);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertNull(circle1.lastOverlapCircle);
        assertNull(circle2.lastOverlapCircle);
    }

    private static class SpyCircle implements OverlapDetection.Circle {
        private double centerX;
        private double centerY;
        private double radius;
        OverlapDetection.Circle lastOverlapCircle = null;
        double lastOverlap = -1;

        SpyCircle(double radius, double centerX, double centerY) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
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
        public double getRadius() {
            return radius;
        }

        @Override
        public void onOverlap(OverlapDetection.Circle circle, double overlap) {
            lastOverlapCircle = circle;
            lastOverlap = overlap;
        }
    }
}