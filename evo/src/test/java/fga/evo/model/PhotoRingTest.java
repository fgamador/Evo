package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoRingTest extends EvoTest {
    @Test
    public void testCalcPhotoAbsorptivity_NoInnerRing() {
        assertEquals(0.5, new PhotoRing(1).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new PhotoRing(3).calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity_WithInnerRing() {
        FloatRing innerRing = new FloatRing(1);
        innerRing.syncFields(null);
        PhotoRing ring = new PhotoRing(2);
        ring.syncFields(innerRing);

        assertEquals(0.5, ring.calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testPhotosynthesize() {
        assertEquals(4.5, new PhotoRing(3).photosynthesize(2), 0);
    }
}
