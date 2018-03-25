package fga.evo.model.geometry;

import fga.evo.model.physics.Ball;

import static fga.evo.model.util.Util.sqr;

public class Circles {
    public static boolean circlesOverlap(Circle circle1, Circle circle2, double centerSeparationSquared) {
        return sqr(circle1.getRadius() + circle2.getRadius()) > centerSeparationSquared;
    }

    public static double calcOverlap(Circle circle1, Circle circle2) {
        return calcOverlap(circle1, circle2, Math.sqrt(calcCenterSeparationSquared(circle1, circle2)));
    }

    public static double calcCenterSeparationSquared(Circle circle1, Circle circle2) {
        double ball1RelativeCenterX = circle1.getCenterX() - circle2.getCenterX();
        double ball1RelativeCenterY = circle1.getCenterY() - circle2.getCenterY();
        return sqr(ball1RelativeCenterX) + sqr(ball1RelativeCenterY);
    }

    public static double calcOverlap(Circle circle1, Circle circle2, double centerSeparation) {
        return circle1.getRadius() + circle2.getRadius() - centerSeparation;
    }

    public static double calcCenterSeparation(Circle circle1, Circle circle2, double overlap) {
        return circle1.getRadius() + circle2.getRadius() - overlap;
    }

    public static double minX(Circle circle) {
        return circle.getCenterX() - circle.getRadius();
    }

    public static double maxX(Circle circle) {
        return circle.getCenterX() + circle.getRadius();
    }

    public static double calcCenterSeparation(Ball ball1, Ball ball2) {
        return Math.sqrt(calcCenterSeparationSquared(ball1, ball2));
    }
}
