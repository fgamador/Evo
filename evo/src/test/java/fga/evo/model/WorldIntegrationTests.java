package fga.evo.model;

import org.junit.Before;

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

    protected Cell addCell(double radius, CellControl control) {
        Cell cell = new Cell(radius, control);
        world.addCell(cell);
        return cell;
    }
}
