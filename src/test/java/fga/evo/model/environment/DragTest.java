package fga.evo.model.environment;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.Cell;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class DragTest extends EvoTest {
    @Before
    public void setUp() {
        Drag.dragFactor.setValue(0.1);
    }

    @Test
    public void dragAddsNoForceIfNoVelocity() {
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);

        new Drag().updateEnvironment(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void dragAddsForceOpposingVelocity() {
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        cell.setVelocity(-2, 3);

        new Drag().updateEnvironment(cell);

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), cell);
    }

    @Test
    public void dragForceUsesDragFactor() {
        Drag.dragFactor.setValue(2);
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        cell.setVelocity(1, 0);

        new Drag().updateEnvironment(cell);

        assertNetForce(-4, 0, cell);
    }
}
