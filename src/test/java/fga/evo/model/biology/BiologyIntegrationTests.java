package fga.evo.model.biology;

import fga.evo.model.Assert;
import fga.evo.model.WorldIntegrationTests;
import fga.evo.model.control.FixedDepthSeekingControl;
import fga.evo.model.control.ParentChildControl;
import fga.evo.model.environment.Illumination;
import fga.evo.model.environment.Weight;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BiologyIntegrationTests extends WorldIntegrationTests {
    @Before
    public void setUp() {
        Illumination.maxIntensity.setValue(2);
        PhotoRing.parameters.density.setValue(0.011);
        Weight.fluidDensity.setValue(0.01);
        Weight.gravity.setValue(0.1);
    }

    @Test
    public void photosyntheticGrowth() {
        world.addEnvironmentalInfluence(new Illumination(10));
        Cell cell = new Cell.Builder() //
                .withControl(c -> c.requestPhotoAreaResize(1000)) //
                .withPhotoRingArea(Math.PI) //
                .build();
        world.addCell(cell);

        cell.setCenterPosition(5, -5);
        world.tick();

        final double addedLightEnergy = 0.5;
        final double photoRingMaintenanceEnergy = Math.PI * PhotoRing.parameters.maintenanceCost.getValue();
        final double energyBudget = addedLightEnergy - photoRingMaintenanceEnergy;
        final double addedPhotoRingArea = energyBudget / PhotoRing.parameters.growthCost.getValue();
        assertEquals(Math.PI + addedPhotoRingArea, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void buoyancyControlMovesCellDownward() {
        world.addForceInfluence(new Weight());
        Cell cell = new Cell.Builder() //
                .withControl(new FixedDepthSeekingControl(100)) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(100) //
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
    public void buoyancyControlMovesCellUpward() {
        world.addForceInfluence(new Weight());
        Cell cell = new Cell.Builder() //
                .withControl(new FixedDepthSeekingControl(100)) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(100) //
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
    public void reproduction() {
        Cell cell = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(10) //
                .build();
        world.addCell(cell);
        cell.setCenterPosition(5, -5);

        Collection<Cell> newCells = world.tick();

        assertEquals(1, newCells.size());
        assertEquals(2, world.getCells().size());
        assertEquals(1, world.getBonds().size());
        Cell child = cell.getChild();
        assertEquals(1, child.getRadius(), 0);
        assertEquals(Math.PI * PhotoRing.parameters.density.getValue(), child.getMass(), Assert.DEFAULT_DELTA);
        assertEquals(5, child.getCenterX(), 20);
        assertEquals(-5, child.getCenterY(), 20);
    }

    @Test
    public void growthAfterReproduction() {
        Cell cell = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(10) //
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
