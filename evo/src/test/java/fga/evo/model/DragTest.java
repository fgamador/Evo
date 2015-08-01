package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;
import static org.junit.Assert.assertEquals;

public class DragTest {
    private Cell cell;
    private Drag drag;

    @Before
    public void setUp() {
        cell = new Cell(2);
        cell.setPosition(5, 5);
        drag = new Drag();
    }

    @Test
    public void testAddDragForceToCell_NoMotion() {
        drag.addForcesToCell(cell);

        assertForce(0, 0, cell);
    }

    @Test
    public void testAddDragForceToCell_Motion() {
        cell.setVelocity(-2, 3);

        drag.addForcesToCell(cell);

        assertForce(8 * Drag.getDragFactor(), -18 * Drag.getDragFactor(), cell);
    }

    @Test
    public void testAddDragForceToCell_DragFactor() {
        double defaultDragFactor = Drag.getDragFactor();
        try {
            Drag.setDragFactor(2);
            cell.setVelocity(1, 0);

            drag.addForcesToCell(cell);

            assertForce(-4, 0, cell);
        } finally {
            Drag.setDragFactor(defaultDragFactor);
        }
    }
}
