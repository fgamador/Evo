package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Test
    public void testPhotosyntheticGrowth() {
        Cell cell = addCell(1, 3);
        cell.setPosition(7, 7);
        // TODO install simple brains

        world.tick();

        //assertEnergy(0, cell);
        // TODO check cell size
    }
}
