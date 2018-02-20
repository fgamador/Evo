package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
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
        drag.updateEnvironment(cell.getEnvironment());

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddDragForceToCell_Motion() {
        cell.setVelocity(-2, 3);

        drag.updateEnvironment(cell.getEnvironment());

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), cell);
    }

    @Test
    public void testAddDragForceToCell_DragFactor() {
        Drag.dragFactor.setValue(2);
        cell.setVelocity(1, 0);

        drag.updateEnvironment(cell.getEnvironment());

        assertNetForce(-4, 0, cell);
    }
}
