package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static org.junit.Assert.assertEquals;

public class PhotoRingTest {
//    @Test
//    public void testFoo() {
//        PhotoRing ring = new PhotoRing();
//        ring.areaToOuterRadius(1);
//        assertEquals(1, ring.getOuterRadius(), 0);
//    }

    @Test
    public void testGetArea() {
        PhotoRing ring = new PhotoRing(1);
        ring.outerRadiusToArea();
        assertEquals(Math.PI, ring.getArea(), 0);

        ring = new PhotoRing(2);
        ring.outerRadiusToArea();
        assertEquals(Math.PI * 4, ring.getArea(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity() {
        assertEquals(0.5, new PhotoRing(1).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new PhotoRing(3).calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testPhotosynthesize() {
        assertEquals(4.5, new PhotoRing(3).photosynthesize(2), 0);
    }

    @Test
    public void testGetMaintenanceEnergy() {
        PhotoRing ring = new PhotoRing(3);
        ring.outerRadiusToArea();
        assertEquals(Math.PI * 9 * PhotoRing.getMaintenanceCost(), ring.getMaintenanceEnergy(), 0);
    }

//    @Test
//    public void testGrowPhotoRing() {
//        Cell cell = new Cell(1);
//        cell.addEnergy(3);
//
//        cell.growPhotoRing(2);
//
//        assertEquals(Math.PI + 2 / PhotoRing.getGrowthCost(), cell.getPhotoRingArea(), 0);
//        assertEnergy(1, cell);
//    }
}
