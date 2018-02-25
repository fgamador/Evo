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
        Drag drag = new Drag();
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        CellEnvironment environment = new CellEnvironment();

        drag.updateEnvironment(environment, cell);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void dragAddsForceOpposingVelocity() {
        Drag drag = new Drag();
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        cell.setVelocity(-2, 3);
        CellEnvironment environment = new CellEnvironment();

        drag.updateEnvironment(environment, cell);

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), environment);
    }

    @Test
    public void dragForceUsesDragFactor() {
        Drag.dragFactor.setValue(2);
        Drag drag = new Drag();
        Cell cell = new Cell(2);
        cell.setCenterPosition(0, -10);
        cell.setVelocity(1, 0);
        CellEnvironment environment = new CellEnvironment();

        drag.updateEnvironment(environment, cell);

        assertNetForce(-4, 0, environment);
    }
}
