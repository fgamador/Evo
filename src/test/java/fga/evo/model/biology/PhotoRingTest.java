package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.FloatRing;
import fga.evo.model.biology.PhotoRing;
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
        PhotoRing testSubject = new PhotoRing(0);
        testSubject.setArea(Math.PI * 9);
        testSubject.syncFields(null);
        assertEquals(4.5, testSubject.photosynthesize(2), 0);
    }
}
