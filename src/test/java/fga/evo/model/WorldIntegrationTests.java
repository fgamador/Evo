package fga.evo.model;

import org.junit.Before;

public abstract class WorldIntegrationTests extends EvoTest {
    protected World world;

    @Before
    public void worldSetUp() {
        world = new World();
    }

    protected Cell addCell(double radius) {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(radius)
                .build();
        world.addCell(cell);
        return cell;
    }
}
