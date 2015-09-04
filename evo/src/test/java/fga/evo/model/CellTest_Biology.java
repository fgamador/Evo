package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static fga.evo.model.Util.sqr;
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
        final double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(growthEnergy));
        assertEquals(0, cell.getFloatArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(growthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(100 - growthEnergy, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        final double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(growthEnergy));
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(Math.PI + growthEnergy / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEquals(100 - growthEnergy, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingShrinkage() {
        final double radius = 2;
        final double shrinkageEnergy = 0.1;
        Cell cell = new Cell(radius, c -> c.requestPhotoAreaResize(-shrinkageEnergy));
        final double area = Math.PI * sqr(radius);
        assertEquals(area, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(area - shrinkageEnergy / PhotoRing.parameters.getShrinkageYield(), cell.getPhotoArea(), 0);
        assertEquals(shrinkageEnergy, cell.getEnergy(), 0);
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
        final double floatGrowthEnergy = 3;
        final double photoGrowthEnergy = 2;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(photoGrowthEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(100);

        cell.useEnergy();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(Math.PI + photoGrowthEnergy / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEquals(100 - (floatGrowthEnergy + photoGrowthEnergy), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_OffsettingRequests() {
        final double floatGrowthEnergy = 0.1;
        final double photoShrinkageEnergy = 0.1;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(-photoShrinkageEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(Math.PI - photoShrinkageEnergy / PhotoRing.parameters.getShrinkageYield(), cell.getPhotoArea(), 0);
        assertEquals(floatGrowthEnergy - photoShrinkageEnergy, cell.getEnergy(), 0);
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
