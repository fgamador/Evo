package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing(Math.PI);
        assertEquals(Math.PI, testSubject.getArea(), 0.001);
    }

    @Test
    public void settingAreaClearsOuterRadius() {
        Ring testSubject = new TestRing(Math.PI);
        assertEquals(0, testSubject.getOuterRadius(), 0);
    }

    @Test
    public void syncFieldsSetsOuterRadiusAndMassFromArea() {
        Ring testSubject = createSyncedRing(Math.PI);

        assertEquals(Math.PI, testSubject.getArea(), 0.001);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusInclusiveOfInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(Math.PI);
        Ring testSubject = new TestRing(3 * Math.PI);

        testSubject.syncFields(innerRing);

        assertEquals(2, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusOfZeroAreaRingToEqualInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(Math.PI);
        Ring testSubject = new TestRing((double) 0);

        testSubject.syncFields(innerRing);

        assertEquals(innerRing.getOuterRadius(), testSubject.getOuterRadius(), 0);
        assertEquals(0, testSubject.getArea(), 0);
    }

    private Ring createSyncedRing(double area) {
        Ring ring = new TestRing(area);
        ring.syncFields(null);
        return ring;
    }
}
