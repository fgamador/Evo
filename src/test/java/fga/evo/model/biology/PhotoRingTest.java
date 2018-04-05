package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
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
    public void photosynthesisAddsEnergyToCell() {
        TissueRingCellApiWithEnvironment cell = new TissueRingCellApiWithEnvironment();
        PhotoRing testSubject = new PhotoRing(Math.PI * 9);
        testSubject.setRadiiBasedOnArea(0);

        cell.getEnvironment().setLightIntensity(2);
        testSubject.updateFromEnvironment(cell);

        assertApproxEquals(4.5, cell.energy);
    }
}
