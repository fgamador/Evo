package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoRingTest extends EvoTest {
    @Test
    public void testCalcPhotoAbsorptivity_NoInnerRing() {
        PhotoRing testSubject = new PhotoRing(Math.PI);
        testSubject.setRadiiBasedOnArea(0);
        assertEquals(0.5, testSubject.calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity_NoInnerRing2() {
        PhotoRing testSubject = new PhotoRing(Math.PI * 9);
        testSubject.setRadiiBasedOnArea(0);
        assertEquals(0.75, testSubject.calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testCalcPhotoAbsorptivity_WithInnerRing() {
        FloatRing innerRing = new FloatRing(Math.PI);
        innerRing.setRadiiBasedOnArea(0);
        PhotoRing ring = new PhotoRing(Math.PI * 3);
        ring.setRadiiBasedOnArea(innerRing.getOuterRadius());

        assertEquals(0.5, ring.calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void photosynthesisReturnsTheRightAmountOfEnergy() {
        PhotoRing testSubject = new PhotoRing(Math.PI * 9);
        testSubject.setRadiiBasedOnArea(0);
        assertEquals(4.5, testSubject.photosynthesize(2), 0);
    }
}
