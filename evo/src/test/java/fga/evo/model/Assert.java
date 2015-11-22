package fga.evo.model;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Additional evo-specific assertions.
 */
public class Assert {
    public static final double DEFAULT_DELTA = 0.00001;

//    public static void assertEquals(double expected, double actual) {
//        assertEquals(expected, actual, DEFAULT_DELTA);
//    }

    public static void assertBonded(Cell cell1, Cell cell2) {
        assertTrue(cell1.getBondedCells().contains(cell2));
        assertTrue(cell2.getBondedCells().contains(cell1));
    }

    public static void assertNotBonded(Cell cell1, Cell cell2) {
        assertFalse(cell1.getBondedCells().contains(cell2));
        assertFalse(cell2.getBondedCells().contains(cell1));
    }

    public static void assertCenterSeparation(double expected, Cell cell1, Cell cell2, double delta) {
        final double relativeCenterX = cell1.getCenterX() - cell2.getCenterX();
        final double relativeCenterY = cell1.getCenterY() - cell2.getCenterY();
        final double centerSeparation = Math.sqrt(sqr(relativeCenterX) + sqr(relativeCenterY));
        assertEquals(expected, centerSeparation, delta);
    }

    public static void assertEnergy(double energy, Cell cell) {
        assertEnergy(energy, cell, DEFAULT_DELTA);
    }

    public static void assertEnergy(double energy, Cell cell, double delta) {
        assertEquals("Energy", energy, cell.getEnergy(), delta);
    }

    public static void assertPosition(double centerX, double centerY, AbstractBall ball) {
        assertPosition(centerX, centerY, ball, DEFAULT_DELTA);
    }

    public static void assertPosition(double centerX, double centerY, AbstractBall ball, double delta) {
        assertEquals("Center X", centerX, ball.getCenterX(), delta);
        assertEquals("Center Y", centerY, ball.getCenterY(), delta);
    }

    public static void assertVelocity(double velocityX, double velocityY, AbstractBall ball) {
        assertVelocity(velocityX, velocityY, ball, DEFAULT_DELTA);
    }

    public static void assertVelocity(double velocityX, double velocityY, AbstractBall ball, double delta) {
        assertEquals("Velocity X", velocityX, ball.getVelocityX(), delta);
        assertEquals("Velocity Y", velocityY, ball.getVelocityY(), delta);
    }

    public static void assertNetForce(double forceX, double forceY, AbstractBall ball) {
        assertNetForce(forceX, forceY, ball, DEFAULT_DELTA);
    }

    public static void assertNetForce(double forceX, double forceY, AbstractBall ball, double delta) {
        assertEquals("Force X", forceX, ball.getNetForceX(), delta);
        assertEquals("Force Y", forceY, ball.getNetForceY(), delta);
    }
}
