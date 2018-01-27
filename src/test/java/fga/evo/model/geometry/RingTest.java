package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertEquals(Math.PI, testSubject.getArea(), 0.001);
    }

    // TODO Do I care about this any more?
    @Test
    public void settingAreaClearsOuterRadius() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertEquals(0, testSubject.getOuterRadius(), 0);
    }

    @Test
    public void syncFieldsSetsOuterRadiusAndMassFromArea() {
        Ring testSubject = new TestRing(Math.PI);

        testSubject.updateRadii(0);

        assertEquals(Math.PI, testSubject.getArea(), 0.001);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusInclusiveOfInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(Math.PI);
        Ring testSubject = new TestRing(3 * Math.PI);

        testSubject.updateRadii(innerRing.getOuterRadius());

        assertEquals(2, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusOfZeroAreaRingToEqualInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(Math.PI);
        Ring testSubject = new TestRing(0);

        testSubject.updateRadii(innerRing.getOuterRadius());

        assertEquals(innerRing.getOuterRadius(), testSubject.getOuterRadius(), 0);
        assertEquals(0, testSubject.getArea(), 0);
    }

    private Ring createSyncedRing(double area) {
        Ring ring = new TestRing(area);
        ring.updateRadii(0);
        return ring;
    }
}
