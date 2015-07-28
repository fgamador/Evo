package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;

public class CellTest_Biology {
//    public static final double SQRT_2 = Math.sqrt(2);

    @Test
    public void testGetPhotoRingArea() {
        assertEquals(Math.PI, new Cell(1).getPhotoRingArea(), 0);
        assertEquals(Math.PI * 4, new Cell(2).getPhotoRingArea(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity() {
        assertEquals(0.5, new Cell(1).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new Cell(3).calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell(3);
        cell.photosynthesize(2);
        assertEnergy(4.5, cell);
    }

    @Test
    public void testSubtractMaintenanceEnergy() {
        Cell cell = new Cell(3);
        cell.subtractMaintenanceEnergy();
        assertEnergy(-Math.PI * 9 * Cell.getPhotoRingCostFactor(), cell);
    }

    @Test
    public void testUseEnergy_SimpleGrowth() {
        Cell cell = new Cell(1);
        assertEquals(Math.PI, cell.getPhotoRingArea(), 0);
        cell.addEnergy(2);
        // TODO add simple control

        cell.useEnergy();

        assertEquals(Math.PI + 2 / Cell.getPhotoRingGrowthCostFactor(), cell.getPhotoRingArea(), 0);
        assertEnergy(0, cell);
    }
}
