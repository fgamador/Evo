package fga.evo.model.geometry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fga.evo.model.Assert.assertEmpty;

public class OverlapDetectionTest {
    @Test
    public void notTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 3, -3);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEmpty(circle1.overlapCircles);
        assertEmpty(circle2.overlapCircles);
    }

    @Test
    public void justTouchingNoOverlap() {
        SpyCircle circle1 = new SpyCircle(1, 0, 0);
        SpyCircle circle2 = new SpyCircle(1, 2, 0);

        OverlapDetection testSubject = new OverlapDetection();
        testSubject.addCircles(Arrays.asList(circle1, circle2));
        testSubject.findAndNotifyOverlaps();

        assertEmpty(circle1.overlapCircles);
        assertEmpty(circle2.overlapCircles);
    }

    private static class SpyCircle implements OverlapDetection.Circle {
        private double centerX;
        private double centerY;
        private double radius;
        List<OverlapDetection.Circle> overlapCircles = new ArrayList<>();

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
        public void onPossibleOverlap(OverlapDetection.Circle circle) {
            overlapCircles.add(circle);
        }
    }
}