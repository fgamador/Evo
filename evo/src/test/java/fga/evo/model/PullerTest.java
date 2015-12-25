package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class PullerTest {
    private Cell cell;
    private Puller puller;

    @Before
    public void setUp() {
        Puller.pullerForceFactor.setValue(1);

        cell = new Cell(1);
        puller = new Puller(cell);
    }

    @After
    public void tearDown() {
        Puller.pullerForceFactor.revertToDefaultValue();
    }

    @Test
    public void testAddForceToCell_NoPull() {
        cell.setCenterPosition(5, -5);
        puller.setPosition(5, -5);

        puller.addForceToCell();

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddForceToCell_Pull() {
        cell.setCenterPosition(5, -5);
        puller.setPosition(6, -6);

        puller.addForceToCell();

        assertNetForce(1, -1, cell);
    }

    @Test
    public void testAddForceToCell_PullForceFactor() {
        cell.setCenterPosition(5, -5);
        puller.setPosition(6, -6);
        Puller.pullerForceFactor.setValue(2);

        puller.addForceToCell();

        assertNetForce(2, -2, cell);
    }
}
