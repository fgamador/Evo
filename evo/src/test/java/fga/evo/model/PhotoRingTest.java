package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoRingTest {
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
        assertEquals(Math.PI * 9 * PhotoRing.parameters.getMaintenanceCost(), ring.getMaintenanceEnergy(), 0);
    }

    @Test
    public void testGrowArea() {
        PhotoRing ring = new PhotoRing(1);
        ring.outerRadiusToArea();

        ring.growArea(2);

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), ring.getArea(), 0);
    }
}
