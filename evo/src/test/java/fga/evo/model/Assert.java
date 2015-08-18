package fga.evo.model;

import static org.junit.Assert.assertEquals;

/**
 * Additional evo-specific assertions.
 */
public class Assert {
    public static final double DEFAULT_DELTA = 0.00001;

    public static void assertEnergy(double energy, Cell cell) {
        assertEnergy(energy, cell, DEFAULT_DELTA);
    }

    public static void assertEnergy(double energy, Cell cell, double delta) {
        assertEquals("Energy", energy, cell.getEnergy(), delta);
    }

    public static void assertNetForce(double forceX, double forceY, Cell cell) {
        assertNetForce(forceX, forceY, cell, DEFAULT_DELTA);
    }

    public static void assertNetForce(double forceX, double forceY, Cell cell, double delta) {
        assertEquals("Force X", forceX, cell.getNetForceX(), delta);
        assertEquals("Force Y", forceY, cell.getNetForceY(), delta);
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
