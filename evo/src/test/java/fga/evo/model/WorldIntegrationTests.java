package fga.evo.model;

import org.junit.Before;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public abstract class WorldIntegrationTests {
    protected World world;

    @Before
    public void baseSetUp() {
        world = new World();
    }

    protected Cell addCell(double radius) {
        Cell cell = new Cell(radius);
        world.addCell(cell);
        return cell;
    }
}
