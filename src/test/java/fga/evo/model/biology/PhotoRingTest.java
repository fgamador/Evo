package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static org.junit.Assert.assertEquals;

public class PhotoRingTest extends EvoTest {
    @Test
    public void photoAbsorptivityWithNoInnerRing() {
        PhotoRing testSubject = new PhotoRing(Math.PI);
        testSubject.setRadiiBasedOnArea(0);
        assertApproxEquals(0.5, testSubject.calcPhotoAbsorptivity());
    }

    @Test
    public void photoAbsorptivityWithNoInnerRingAndLargerArea() {
        PhotoRing testSubject = new PhotoRing(Math.PI * 9);
        testSubject.setRadiiBasedOnArea(0);
        assertApproxEquals(0.75, testSubject.calcPhotoAbsorptivity());
    }

    @Test
    public void photoAbsorptivityWithInnerRing() {
        FloatRing innerRing = new FloatRing(Math.PI);
        innerRing.setRadiiBasedOnArea(0);
        PhotoRing ring = new PhotoRing(Math.PI * 3);
        ring.setRadiiBasedOnArea(innerRing.getOuterRadius());

        assertApproxEquals(0.5, ring.calcPhotoAbsorptivity());
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
