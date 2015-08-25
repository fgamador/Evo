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
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(2));
        assertEquals(0, cell.getFloatArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(2, cell.getFloatArea(), 0);
        assertEquals(100 - 2 * FloatRing.parameters.getGrowthCost(), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(Math.PI + 2));
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(Math.PI + 2, cell.getPhotoArea(), 0);
        assertEquals(100 - 2 * PhotoRing.parameters.getGrowthCost(), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingShrinkage() {
        Cell cell = new Cell(2, c -> c.requestPhotoAreaResize(3 * Math.PI));
        assertEquals(4 * Math.PI, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0);
        assertEquals(Math.PI * PhotoRing.parameters.getShrinkageYield(), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(1000)); // use all energy
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testUseEnergy_FloatAndPhotoRingGrowth() {
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(3);
            c.requestPhotoAreaResize(Math.PI + 2);
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

    @Test
    public void testUseEnergy_OffsettingRequests() {
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(2);
            c.requestPhotoAreaResize(Math.PI - 2);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(2, cell.getFloatArea(), 0);
        assertEquals(Math.PI - 2, cell.getPhotoArea(), 0);
        assertEquals(2 * PhotoRing.parameters.getShrinkageYield() - 2 * FloatRing.parameters.getGrowthCost(),
                cell.getEnergy(), 0);
    }

    // TODO is this useful? need to work out the numbers
//    @Test
//    public void testUseEnergy_FloatAndPhotoRingGrowth_ExcessiveRequest() {
//        ControlApi cell = new ControlApi(1, c -> { // use all energy
//            c.requestFloatAreaResize(1000);
//            c.requestPhotoAreaResize(1000);
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

    // TODO test shrinkage

    @Test
    public void testUseEnergy_FloatRingGrowth2() {
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(1000)); // use all energy
        double oldPhotoRingArea = cell.getPhotoArea();
//        double oldPhotoRingMass = cell.getPhotoRingMass();
        cell.addEnergy(1);

        cell.useEnergy();

        assertTrue(cell.getFloatArea() > 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoArea(), 0);
        assertEquals(cell.getFloatArea() + cell.getPhotoArea(), cell.getArea(), 0.00001);

        assertTrue(cell.getFloatRingOuterRadius() > 0);
        assertTrue(cell.getPhotoRingOuterRadius() > 1);
        assertTrue(cell.getPhotoRingOuterRadius() > cell.getFloatRingOuterRadius());
        assertEquals(cell.getPhotoRingOuterRadius(), cell.getRadius(), 0);

//        assertTrue(cell.getFloatRingMass() > 0);
//        assertEquals(oldPhotoRingMass, cell.getPhotoRingMass(), 0);
//        assertEquals(cell.getFloatRingMass() + cell.getPhotoRingMass(), cell.getMass(), 0.00001);

        assertEquals(cell.getFloatArea() * FloatRing.parameters.getTissueDensity()
                        + cell.getPhotoArea() * PhotoRing.parameters.getTissueDensity(),
                cell.getMass(), 0.00001);

        assertEnergy(0, cell);
    }
}
