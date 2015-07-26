package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;
import static org.junit.Assert.assertEquals;

public class FluidTest {
    private Cell cell;
    private Fluid fluid;

    @Before
    public void setUp() {
        cell = new Cell(1, 2);
        cell.setPosition(5, 5);
        fluid = new Fluid();
    }

    @Test
    public void testAddDragForceToCell_NoMotion() {
        fluid.addDragForceToCell(cell);

        assertForce(0, 0, cell);
    }

    @Test
    public void testAddDragForceToCell_Motion() {
        cell.setVelocity(-2, 3);

        fluid.addDragForceToCell(cell);

        assertForce(8 * Fluid.getDragFactor(), -18 * Fluid.getDragFactor(), cell);
    }

    @Test
    public void testAddDragForceToCell_DragFactor() {
        double defaultDragFactor = Fluid.getDragFactor();
        try {
            Fluid.setDragFactor(2);
            cell.setVelocity(1, 0);

            fluid.addDragForceToCell(cell);

            assertForce(-4, 0, cell);
        } finally {
            Fluid.setDragFactor(defaultDragFactor);
        }
    }
}
