package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class DragTest extends EvoTest {
    private Cell cell;

    @Before
    public void setUp() {
        Drag.dragFactor.setValue(0.1);

        cell = new Cell(2);
        cell.setCenterPosition(0, -10);
    }

    @Test
    public void testAddDragForceToCell_NoMotion() {
        Drag drag = new Drag();
        CellEnvironment environment = new StandaloneCellEnvironment(2, 0, -10);

        drag.updateEnvironment(environment);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void testAddDragForceToCell_Motion() {
        Drag drag = new Drag();
        cell.setVelocity(-2, 3);

        drag.updateEnvironment(cell.getEnvironment());

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), cell);
    }

    @Test
    public void testAddDragForceToCell_DragFactor() {
        Drag.dragFactor.setValue(2);
        Drag drag = new Drag();
        cell.setVelocity(1, 0);

        drag.updateEnvironment(cell.getEnvironment());

        assertNetForce(-4, 0, cell);
    }
}
