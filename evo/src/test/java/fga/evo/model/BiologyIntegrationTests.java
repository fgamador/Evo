package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Test
    public void testPhotosyntheticGrowth() {
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = addCell(1, c -> c.growPhotoRing(c.getEnergy()));
        // TODO install simple brains (currently built into world tick)
        cell.setPosition(5, 5);

        world.tick();

        assertEquals(Math.PI + 0.5 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRing().getArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testBuoyancyControl() {
        world.addEnvironmentalInfluence(new Buoyancy());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));

        cell.setPosition(100, 100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

//        cell.setPosition(100, 150);
//        world.tick();
//
//        assertTrue(cell.getVelocityY() < 0);
    }
}
