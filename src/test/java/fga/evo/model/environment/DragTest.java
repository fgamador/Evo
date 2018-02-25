package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
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
        CellEnvironment environment = new StandaloneCellEnvironment(2, 0, -10);

        drag.updateEnvironment(environment);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void dragAddsForceOpposingVelocity() {
        Drag drag = new Drag();
        StandaloneCellEnvironment environment = new StandaloneCellEnvironment(2, 0, -10);
        environment.setVelocity(-2, 3);

        drag.updateEnvironment(environment);

        assertNetForce(8 * Drag.dragFactor.getValue(), -18 * Drag.dragFactor.getValue(), environment);
    }

    @Test
    public void dragForceUsesDragFactor() {
        Drag.dragFactor.setValue(2);
        Drag drag = new Drag();
        StandaloneCellEnvironment environment = new StandaloneCellEnvironment(2, 0, -10);
        environment.setVelocity(1, 0);

        drag.updateEnvironment(environment);

        assertNetForce(-4, 0, environment);
    }
}
