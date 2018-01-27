package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.util.Util.sqr;
import static org.junit.Assert.assertEquals;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing();
        testSubject.setArea(Math.PI);
        assertEquals(Math.PI, testSubject.getArea(), 0.001);
    }

    @Test
    public void settingAreaClearsOuterRadius() {
        Ring testSubject = new TestRing();
        testSubject.setArea(Math.PI);
        assertEquals(0, testSubject.getOuterRadius(), 0);
    }

    @Test
    public void syncFieldsSetsOuterRadiusAndMassFromArea() {
        Ring testSubject = new TestRing();
        testSubject.setArea(Math.PI);

        testSubject.syncFields(null);

        assertEquals(Math.PI, testSubject.getArea(), 0.001);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusInclusiveOfInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(1);
        Ring testSubject = new TestRing();
        testSubject.setArea(3 * Math.PI);

        testSubject.syncFields(innerRing);

        assertEquals(2, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusOfZeroAreaRingToEqualInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(1);
        Ring testSubject = new TestRing();

        testSubject.syncFields(innerRing);

        assertEquals(innerRing.getOuterRadius(), testSubject.getOuterRadius(), 0);
        assertEquals(0, testSubject.getArea(), 0);
    }

    private Ring createSyncedRing(double outerRadius) {
        Ring ring = new TestRing();
        ring.setArea(Math.PI * sqr(outerRadius));
        ring.syncFields(null);
        return ring;
    }
}
