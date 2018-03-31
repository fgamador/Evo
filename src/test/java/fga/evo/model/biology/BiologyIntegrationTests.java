package fga.evo.model.biology;

import fga.evo.model.WorldIntegrationTests;
import fga.evo.model.control.FixedDepthSeekingControl;
import fga.evo.model.control.ParentChildControl;
import fga.evo.model.environment.Illumination;
import fga.evo.model.environment.Weight;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

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
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestPhotoAreaResize(1000))
                .setPhotoRingArea(Math.PI)
                .build();
        world.addCell(cell);

        cell.setCenterPosition(5, -5);
        world.tick();

        double addedLightEnergy = 0.5;
        double photoRingMaintenanceEnergy = Math.PI * PhotoRing.parameters.maintenanceCost.getValue();
        double energyBudget = addedLightEnergy - photoRingMaintenanceEnergy;
        double addedPhotoRingArea = energyBudget / PhotoRing.parameters.growthCost.getValue();
        assertEquals(Math.PI + addedPhotoRingArea, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuoyancyControl_Deeper() {
        world.addEnvironmentalInfluence(new Weight());
        Cell cell = new Cell.Builder()
                .setControl(new FixedDepthSeekingControl(100))
                .setPhotoRingArea(Math.PI)
                .setEnergy(100)
                .build();
        world.addCell(cell);

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
        Cell cell = new Cell.Builder()
                .setControl(new FixedDepthSeekingControl(100))
                .setPhotoRingArea(Math.PI)
                .setEnergy(100)
                .build();
        world.addCell(cell);

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
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(10)
                .build();
        world.addCell(cell);
        cell.setCenterPosition(5, -5);

        Collection<Cell> newCells = world.tick();

        assertEquals(1, newCells.size());
        assertEquals(2, world.getCells().size());
        assertEquals(1, world.getBonds().size());
        Cell child = cell.getChild();
        assertEquals(0, child.getRadius(), 0);
        assertEquals(0, child.getMass(), 0);
        assertEquals(5, child.getCenterX(), 20);
        assertEquals(-5, child.getCenterY(), 20);
    }

    @Test
    public void testGrowthAfterReproduction() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(10)
                .build();
        world.addCell(cell);
        cell.setCenterPosition(5, -5);

        world.tick();
        Collection<Cell> newCells = world.tick();

        assertEquals(0, newCells.size());
        assertEquals(2, world.getCells().size());
        assertEquals(1, world.getBonds().size());
        Cell child = cell.getChild();
        assertTrue(child.getRadius() > 0);
        assertTrue(child.getMass() > 0);
    }
}
