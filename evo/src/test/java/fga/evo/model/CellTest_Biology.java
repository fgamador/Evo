package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;

public class CellTest_Biology {
//    public static final double SQRT_2 = Math.sqrt(2);

    // TODO dupe?
    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell(3);
        cell.photosynthesize(2);
        assertEnergy(4.5, cell);
    }

    // TODO dupe?
    @Test
    public void testSubtractMaintenanceEnergy() {
        Cell cell = new Cell(3);
        cell.subtractMaintenanceEnergy();
        assertEnergy(-Math.PI * 9 * PhotoRing.getMaintenanceCost(), cell);
    }

    @Test
    public void testGrowPhotoRing() {
        Cell cell = new Cell(1);
        cell.addEnergy(3);

        cell.growPhotoRing(2);

        assertEquals(Math.PI + 2 / PhotoRing.getGrowthCost(), cell.getPhotoRingArea(), 0);
        assertEnergy(1, cell);
    }

    @Test
    public void testUseEnergy_SimpleGrowth() {
        Cell cell = new Cell(1);
        assertEquals(Math.PI, cell.getPhotoRingArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy(c -> c.growPhotoRing(c.getEnergy()));

        assertEquals(Math.PI + 2 / PhotoRing.getGrowthCost(), cell.getPhotoRingArea(), 0);
        assertEnergy(0, cell);
    }
}
