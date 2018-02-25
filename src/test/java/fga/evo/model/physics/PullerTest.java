package fga.evo.model.physics;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class PullerTest extends EvoTest {
    @Before
    public void setUp() {
        Puller.forceFactor.setValue(1);
    }

    @Test
    public void testAddForceToCell_NoPull() {
        Cell cell = new Cell(1);
        Puller puller = new Puller(cell.getEnvironment());

        cell.setCenterPosition(5, -5);
        puller.setPosition(5, -5);

        puller.addForce();

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddForceToCell_Pull() {
        Cell cell = new Cell(1);
        Puller puller = new Puller(cell.getEnvironment());

        cell.setCenterPosition(5, -5);
        puller.setPosition(6, -6);

        puller.addForce();

        assertNetForce(1, -1, cell);
    }

    @Test
    public void testAddForceToCell_PullForceFactor() {
        Cell cell = new Cell(1);
        Puller puller = new Puller(cell.getEnvironment());

        cell.setCenterPosition(5, -5);
        puller.setPosition(6, -6);
        Puller.forceFactor.setValue(2);

        puller.addForce();

        assertNetForce(2, -2, cell);
    }

    private static class Environment extends NewtonianBodyEnvironment {
        private double centerX;
        private double centerY;

        public void setCenterPosition(double centerX, double centerY) {
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
    }
}
