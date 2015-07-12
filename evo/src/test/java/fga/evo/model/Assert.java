package fga.evo.model;

import static org.junit.Assert.assertEquals;

/**
 * Additional evo-specific assertions.
 */
public class Assert {
    public static final double DEFAULT_DELTA = 0.00001;

    public static void assertForce(double forceX, double forceY, Cell cell) {
        assertForce(forceX, forceY, cell, DEFAULT_DELTA);
    }

    public static void assertForce(double forceX, double forceY, Cell cell, double delta) {
        assertEquals("Force X", forceX, cell.getForceX(), delta);
        assertEquals("Force Y", forceY, cell.getForceY(), delta);
    }

    public static void assertPosition(double centerX, double centerY, Cell cell) {
        assertPosition(centerX, centerY, cell, DEFAULT_DELTA);
    }

    public static void assertPosition(double centerX, double centerY, Cell cell, double delta) {
        assertEquals("Center X", centerX, cell.getCenterX(), delta);
        assertEquals("Center Y", centerY, cell.getCenterY(), delta);
    }

    public static void assertVelocity(double velocityX, double velocityY, Cell cell) {
        assertVelocity(velocityX, velocityY, cell, DEFAULT_DELTA);
    }

    public static void assertVelocity(double velocityX, double velocityY, Cell cell, double delta) {
        assertEquals("Velocity X", velocityX, cell.getVelocityX(), delta);
        assertEquals("Velocity Y", velocityY, cell.getVelocityY(), delta);
    }
}
