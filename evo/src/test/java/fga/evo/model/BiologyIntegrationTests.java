package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.*;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    private double defaultMaxIntensity;

    @Before
    public void setUp() {
        defaultMaxIntensity = Illumination.getMaxIntensity();
    }

    @After
    public void tearDown() {
        Illumination.setMaxIntensity(defaultMaxIntensity);
    }

    @Test
    public void testPhotosyntheticGrowth() {
        Illumination.setMaxIntensity(2);
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = addCell(1, c -> c.requestPhotoAreaResize(1000));
        cell.setCenterPosition(5, -5);

        world.tick();

        assertEquals(Math.PI + 0.5 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testBuoyancyControl_Deeper() {
        world.addEnvironmentalInfluence(new Weight());
        Cell cell = addCell(1, new FixedDepthSeekingControl(100));
        cell.addEnergy(100);

        cell.setCenterPosition(100, -100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setCenterPosition(100, -101);
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

        cell.setCenterPosition(100, -100);
        world.tick();

        assertEquals(0, cell.getVelocityY(), 0.01);

        cell.setCenterPosition(100, -99);
        world.tick();
        world.tick();
        world.tick();
        world.tick();

        double velocityY_99 = cell.getVelocityY();
        assertTrue(velocityY_99 < 0);
    }

    @Test
    public void testReproduction() {
        double spawnOdds = 1;
        double donation = 2;
        Cell cell = addCell(10, new ParentChildControl(spawnOdds, donation));
        cell.setCenterPosition(5, -5);
        cell.addEnergy(10);

        Collection<Cell> newCells = world.tick();

        assertEquals(1, newCells.size());
        assertEquals(2, world.getCells().size());
        Cell child = cell.getChild();
        assertEquals(0, child.getRadius(), 0);
        assertEquals(0, child.getMass(), 0);
        assertEquals(5, child.getCenterX(), 20);
        assertEquals(-5, child.getCenterY(), 20);
    }

    @Test
    public void testGrowthAfterReproduction() {
        double spawnOdds = 1;
        double donation = 2;
        Cell cell = addCell(10, new ParentChildControl(spawnOdds, donation));
        cell.setCenterPosition(5, -5);
        cell.addEnergy(10);

        world.tick();
        Collection<Cell> newCells = world.tick();

        assertEquals(0, newCells.size());
        assertEquals(2, world.getCells().size());
        Cell child = cell.getChild();
        assertTrue(child.getRadius() > 0);
        assertTrue(child.getMass() > 0);
    }
}
