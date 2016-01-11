package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class DragTest extends EvoTest {
    private Cell cell;
    private Drag drag;

    @Before
    public void setUp() {
        Drag.dragFactor.setValue(0.1);

        cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        drag = new Drag();
    }

    @Test
    public void testAddDragForceToCell_NoMotion() {
        drag.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddDragForceToCell_Motion() {
        cell.setVelocity(-2, 3);

        drag.addForcesToCell(cell);

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), cell);
    }

    @Test
    public void testAddDragForceToCell_DragFactor() {
        Drag.dragFactor.setValue(2);
        cell.setVelocity(1, 0);

        drag.addForcesToCell(cell);

        assertNetForce(-4, 0, cell);
    }
}
