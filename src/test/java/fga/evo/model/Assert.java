package fga.evo.model;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.NewtonianBody;
import fga.evo.model.physics.NewtonianBodyEnvironment;

import java.util.Collection;

import static fga.evo.model.util.Util.sqr;
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

    public static void assertEmpty(Collection<?> collection) {
        assertTrue(collection.isEmpty());
    }

    public static <T> void assertContains(Collection<T> collection, T item) {
        assertTrue(collection.contains(item));
    }

    public static void assertBonded(Cell cell1, Cell cell2) {
        assertTrue(cell1.isBondedTo(cell2));
        assertTrue(cell2.isBondedTo(cell1));
    }

    public static void assertNotBonded(Cell cell1, Cell cell2) {
        assertFalse(cell1.isBondedTo(cell2));
        assertFalse(cell2.isBondedTo(cell1));
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

    public static void assertPosition(double centerX, double centerY, NewtonianBody body) {
        assertPosition(centerX, centerY, body, DEFAULT_DELTA);
    }

    public static void assertPosition(double centerX, double centerY, NewtonianBody body, double delta) {
        assertEquals("Center X", centerX, body.getCenterX(), delta);
        assertEquals("Center Y", centerY, body.getCenterY(), delta);
    }

    public static void assertVelocity(double velocityX, double velocityY, NewtonianBody body) {
        assertVelocity(velocityX, velocityY, body, DEFAULT_DELTA);
    }

    public static void assertVelocity(double velocityX, double velocityY, NewtonianBody body, double delta) {
        assertEquals("Velocity X", velocityX, body.getVelocityX(), delta);
        assertEquals("Velocity Y", velocityY, body.getVelocityY(), delta);
    }

    public static void assertNetForce(double forceX, double forceY, NewtonianBodyEnvironment environment) {
        assertNetForce(forceX, forceY, environment, DEFAULT_DELTA);
    }

    public static void assertNetForce(double forceX, double forceY, NewtonianBodyEnvironment environment, double delta) {
        assertEquals("Force X", forceX, environment.getNetForceX(), delta);
        assertEquals("Force Y", forceY, environment.getNetForceY(), delta);
    }
}
