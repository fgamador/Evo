package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.*;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Before
    public void setUp() {
        Illumination.maxIntensity.setValue(2);
        PhotoRing.parameters.density.setValue(0.011);
        Weight.fluidDensity.setValue(0.01);
        Weight.gravity.setValue(0.1);
    }

    @Test
    public void testPhotosyntheticGrowth() {
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = addCell(1, c -> c.requestPhotoAreaResize(1000));
        cell.setCenterPosition(5, -5);

        world.tick();

        double initialPhotoRingArea = Math.PI;
        double addedLightEnergy = 0.5;
        double photoRingMaintenanceEnergy = initialPhotoRingArea * PhotoRing.parameters.maintenanceCost.getValue();
        double energyBudget = addedLightEnergy - photoRingMaintenanceEnergy;
        double addedPhotoRingArea = energyBudget / PhotoRing.parameters.growthCost.getValue();
        assertEquals(initialPhotoRingArea + addedPhotoRingArea, cell.getPhotoArea(), 0.001);
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

        assertTrue(cell.getVelocityY() > 0);
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

        assertTrue(cell.getVelocityY() < 0);
    }

    @Test
    public void testReproduction() {
        Cell cell = addCell(10, new ParentChildControl(1, 2));
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
        Cell cell = addCell(10, new ParentChildControl(1, 2));
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
