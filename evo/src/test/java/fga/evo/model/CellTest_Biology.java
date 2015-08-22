package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testUseEnergy_FloatRingGrowth() {
        Cell cell = new Cell(1, c -> c.requestResizeFloatArea(2));
        assertEquals(0, cell.getFloatArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(2, cell.getFloatArea(), 0);
        assertEquals(100 - 2 * FloatRing.parameters.getGrowthCost(), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        Cell cell = new Cell(1, c -> c.requestResizePhotoArea(2));
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(Math.PI + 2, cell.getPhotoArea(), 0);
        assertEquals(100 - 2 * PhotoRing.parameters.getGrowthCost(), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell(1, c -> c.requestResizePhotoArea(1000));
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testUseEnergy_FloatAndPhotoRingGrowth() {
        Cell cell = new Cell(1, c -> {
            c.requestResizeFloatArea(3);
            c.requestResizePhotoArea(2);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(3, cell.getFloatArea(), 0);
        assertEquals(Math.PI + 2, cell.getPhotoArea(), 0);
        assertEquals(100 - 3 * FloatRing.parameters.getGrowthCost() - 2 * PhotoRing.parameters.getGrowthCost(),
                cell.getEnergy(), 0);
    }

    // TODO is this useful? need to work out the numbers
//    @Test
//    public void testUseEnergy_FloatAndPhotoRingGrowth_ExcessiveRequest() {
//        Cell cell = new Cell(1, c -> {
//            c.requestResizeFloatArea(1000);
//            c.requestResizePhotoArea(1000);
//        });
//        assertEquals(0, cell.getFloatArea(), 0);
//        assertEquals(Math.PI, cell.getPhotoArea(), 0);
//        cell.addEnergy(2);
//
//        cell.useEnergy();
//
//        assertEquals(1 / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
//        assertEquals(Math.PI + 1 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
//        assertEnergy(0, cell);
//    }

    @Test
    public void testUseEnergy_FloatRingGrowth2() {
        Cell cell = new Cell(1, c -> c.requestResizeFloatArea(1000));
        double oldPhotoRingArea = cell.getPhotoArea();
        double oldPhotoRingMass = cell.getPhotoRing().getMass();
        cell.addEnergy(1);

        cell.useEnergy();

        assertTrue(cell.getFloatArea() > 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoArea(), 0);
        assertEquals(cell.getFloatArea() + cell.getPhotoArea(), cell.getArea(), 0.00001);

        assertTrue(cell.getFloatRing().getOuterRadius() > 0);
        assertTrue(cell.getPhotoRing().getOuterRadius() > 1);
        assertTrue(cell.getPhotoRing().getOuterRadius() > cell.getFloatRing().getOuterRadius());
        assertEquals(cell.getPhotoRing().getOuterRadius(), cell.getRadius(), 0);

        assertTrue(cell.getFloatRing().getMass() > 0);
        assertEquals(oldPhotoRingMass, cell.getPhotoRing().getMass(), 0);
        assertEquals(cell.getFloatRing().getMass() + cell.getPhotoRing().getMass(), cell.getMass(), 0.00001);

        assertEnergy(0, cell);
    }
}
