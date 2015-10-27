package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.*;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Test
    public void testPhotosyntheticGrowth() {
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = addCell(1, c -> c.requestPhotoAreaResize(1000));
        cell.setPosition(5, -5);

        world.tick();

        assertEquals(Math.PI + 0.5 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testBuoyancyControl_Deeper() {
        world.addEnvironmentalInfluence(new Weight());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));
        cell.addEnergy(100);

        cell.setPosition(100, -100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setPosition(100, -101);
        world.tick();
        world.tick();
        world.tick();
        world.tick();

        double velocityY_101 = cell.getVelocityY();
        assertTrue(velocityY_101 > 0);
    }

    @Test
    public void testBuoyancyControl_Shallower() {
        world.addEnvironmentalInfluence(new Weight());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));
        cell.addEnergy(100);

        cell.setPosition(100, -100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setPosition(100, -99);
        world.tick();
        world.tick();
        world.tick();
        world.tick();

        double velocityY_99 = cell.getVelocityY();
        assertTrue(velocityY_99 < 0);
    }

    @Test
    public void testReproduction() {
        final double donation = 2;
        Cell cell = addCell(10, new ParentChildControl(donation));
        cell.setPosition(5, -5);
        cell.addEnergy(10);

        world.tick();

        assertEquals(2, world.getCells().size());
        Cell child = cell.getChild();
        assertEquals(0, child.getRadius(), 0);

        world.tick();

        assertNotEquals(0, child.getRadius(), 0);
    }
}
