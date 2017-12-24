package fga.evo.model.geometry;

import static fga.evo.model.Util.sqr;

public class Circles {
    public static boolean circlesOverlapWithOffset(Circle circle1, Circle circle2) {
        double centerSeparationSquared = calcCenterSeparationSquared(circle1, circle2);
        return centerSeparationSquared != 0 && circlesOverlap(circle1, circle2, centerSeparationSquared);
    }

    public static boolean circlesOverlap(Circle circle1, Circle circle2, double centerSeparationSquared) {
        return sqr(circle1.getRadius() + circle2.getRadius()) > centerSeparationSquared;
    }

    public static double calcCenterSeparationSquared(Circle circle1, Circle circle2) {
        double ball1RelativeCenterX = circle1.getCenterX() - circle2.getCenterX();
        double ball1RelativeCenterY = circle1.getCenterY() - circle2.getCenterY();
        return sqr(ball1RelativeCenterX) + sqr(ball1RelativeCenterY);
    }
}
