package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public abstract class WorldIntegrationTests {
    protected World world;

    @Before
    public void setUp() {
        world = new World();
    }

    protected Cell addCell(double mass, double radius) {
        Cell cell = new Cell(mass, radius);
        world.addCell(cell);
        return cell;
    }
}
