package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;

public class CellTest_Biology {
//    public static final double SQRT_2 = Math.sqrt(2);

    // TODO dup?
    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell(3);
        cell.photosynthesize(2);
        assertEnergy(4.5, cell);
    }

    // TODO dup?
    @Test
    public void testSubtractMaintenanceEnergy() {
        Cell cell = new Cell(3);
        cell.subtractMaintenanceEnergy();
        assertEnergy(-Math.PI * 9 * PhotoRing.parameters.getMaintenanceCost(), cell);
    }

    @Test
    public void testGrowPhotoRing() {
        Cell cell = new Cell(1);
        double oldPhotoRingArea = cell.getPhotoRingArea();
        cell.addEnergy(3);

        cell.growPhotoRing(2);

        assertEquals(oldPhotoRingArea + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRingArea(), 0);
        assertEnergy(1, cell);
    }

    @Test
    public void testGrowFloatRing() {
        Cell cell = new Cell(1);
        double oldPhotoRingArea = cell.getPhotoRingArea();
        cell.addEnergy(2);

        cell.growFloatRing(1);

        assertEquals(1 / FloatRing.parameters.getGrowthCost(), cell.getFloatRingArea(), 0);
        // TODO radius
        assertEquals(oldPhotoRingArea, cell.getPhotoRingArea(), 0);
        // TODO assertEquals(cell.getFloatRingArea() + cell.getPhotoRingArea(), cell.getArea(), 0);
        assertEnergy(1, cell);
    }

    @Test
    public void testUseEnergy_SimpleGrowth() {
        Cell cell = new Cell(1);
        assertEquals(Math.PI, cell.getPhotoRingArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy(c -> c.growPhotoRing(c.getEnergy()));

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRingArea(), 0);
        assertEnergy(0, cell);
    }
}
