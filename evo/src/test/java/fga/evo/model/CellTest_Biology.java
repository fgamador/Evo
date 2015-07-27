package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;

public class CellTest_Biology {
//    public static final double SQRT_2 = Math.sqrt(2);

    @Test
    public void testGetPhotoRingArea() {
        assertEquals(Math.PI, new Cell(1, 1).getPhotoRingArea(), 0);
        assertEquals(Math.PI * 4, new Cell(1, 2).getPhotoRingArea(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity() {
        assertEquals(0.5, new Cell(1, 1).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new Cell(1, 3).calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell(1, 3);
        cell.photosynthesize(2);
        assertEnergy(4.5, cell);
    }

    @Test
    public void testSubtractMaintenanceEnergy() {
        Cell cell = new Cell(1, 3);
        cell.subtractMaintenanceEnergy();
        assertEnergy(-Math.PI * 9 * Cell.getPhotoRingCostFactor(), cell);
    }

    // TODO no longer valid
    @Test
    public void testUseEnergyUsesAllEnergy() {
        Cell cell = new Cell(1, 3);
        cell.photosynthesize(2);

        cell.useEnergy();

        assertEnergy(0, cell);
    }
}
