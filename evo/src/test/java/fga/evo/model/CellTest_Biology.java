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
    public void testGrowPhotoRing() {
        Cell cell = new Cell(1);
        double oldPhotoRingArea = cell.getPhotoRing().getArea();
        cell.addEnergy(3);

        cell.growPhotoRing(2);

        assertEquals(oldPhotoRingArea + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRing().getArea(), 0);
        assertEnergy(1, cell);
    }

    @Test
    public void testGrowFloatRing() {
        Cell cell = new Cell(1);
        double oldPhotoRingArea = cell.getPhotoRing().getArea();
        cell.addEnergy(2);

        cell.growFloatRing(1);

        assertEquals(1 / FloatRing.parameters.getGrowthCost(), cell.getFloatRing().getArea(), 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoRing().getArea(), 0);
        assertEnergy(1, cell);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        Cell cell = new Cell(1, c -> c.growPhotoRing(c.getEnergy()));
        assertEquals(Math.PI, cell.getPhotoRing().getArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoRing().getArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testUseEnergy_FloatRingGrowth() {
        Cell cell = new Cell(1, c -> c.growFloatRing(c.getEnergy()));
        double oldPhotoRingArea = cell.getPhotoRing().getArea();
        double oldPhotoRingMass = cell.getPhotoRing().getMass();
        cell.addEnergy(1);

        cell.useEnergy();

        assertTrue(cell.getFloatRing().getArea() > 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoRing().getArea(), 0);
        assertEquals(cell.getFloatRing().getArea() + cell.getPhotoRing().getArea(), cell.getArea(), 0.00001);

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
