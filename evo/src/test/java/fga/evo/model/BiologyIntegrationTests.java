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
    public void testBuoyancyControl_Deeper() {
        world.addEnvironmentalInfluence(new Buoyancy());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));

        cell.setPosition(100, 100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setPosition(100, 101);
        world.tick();

        double velocityY_101 = cell.getVelocityY();
        assertTrue(velocityY_101 < 0);

        cell.setPosition(100, 102);
        world.tick();

        double velocityY_102 = cell.getVelocityY();
        assertTrue(velocityY_102 < velocityY_101);
    }

    @Test
    public void testBuoyancyControl_Shallower() {
        world.addEnvironmentalInfluence(new Buoyancy());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));

        cell.setPosition(100, 100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setPosition(100, 99);
        world.tick();

        double velocityY_99 = cell.getVelocityY();
        assertTrue(velocityY_99 > 0);

        cell.setPosition(100, 98);
        world.tick();

        double velocityY_98 = cell.getVelocityY();
        assertTrue(velocityY_98 > velocityY_99);
    }
}
