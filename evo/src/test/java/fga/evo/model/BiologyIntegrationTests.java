package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Test
    public void testPhotosyntheticGrowth() {
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = addCell(1);
        // TODO install simple brains (currently built into world tick)
        cell.setPosition(5, 5);

        world.tick();

        assertEquals(Math.PI + 0.5 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRingArea(), 0);
        assertEnergy(0, cell);
    }
}
