package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing(1);
        testSubject.setArea(Math.PI);
        assertEquals(Math.PI, testSubject.getArea(), 0.001);
    }

    @Test
    public void settingAreaClearsOuterRadius() {
        Ring testSubject = new TestRing(1);
        testSubject.setOuterRadius(2);

        testSubject.setArea(Math.PI);

        assertEquals(0, testSubject.getOuterRadius(), 0);
    }

    @Test
    public void canSetOuterRadius() {
        Ring testSubject = new TestRing(1);
        testSubject.setOuterRadius(2);
        assertEquals(2, testSubject.getOuterRadius(), 0);
    }

    @Test
    public void settingOuterRadiusClearsArea() {
        Ring testSubject = new TestRing(1);
        testSubject.setArea(Math.PI);

        testSubject.setOuterRadius(2);

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void syncFieldsSetsOuterRadiusAndMassFromArea() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);

        testSubject.syncFields(null);

        assertEquals(Math.PI, testSubject.getArea(), 0.001);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void syncFieldsSetsAreaAndMassFromOuterRadius() {
        Ring testSubject = new TestRing(0);
        testSubject.setOuterRadius(1);

        testSubject.syncFields(null);

        assertEquals(Math.PI, testSubject.getArea(), 0.001);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusInclusiveOfInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(1);
        Ring testSubject = new TestRing(0);
        testSubject.setArea(3 * Math.PI);

        testSubject.syncFields(innerRing);

        assertEquals(2, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void syncFieldsSetsAreaExclusiveOfInnerRingArea() {
        Ring innerRing = createSyncedRing(1);
        Ring testSubject = new TestRing(2);

        testSubject.syncFields(innerRing);

        assertEquals(3 * Math.PI, testSubject.getArea(), 0.001);
    }

    @Test
    public void syncFieldsSetsOuterRadiusOfZeroAreaRingToEqualInnerRingOuterRadius() {
        Ring innerRing = createSyncedRing(1);
        Ring testSubject = new TestRing(0);

        testSubject.syncFields(innerRing);

        assertEquals(innerRing.getOuterRadius(), testSubject.getOuterRadius(), 0);
        assertEquals(0, testSubject.getArea(), 0);
    }

    private Ring createSyncedRing(double outerRadius) {
        Ring ring = new TestRing(outerRadius);
        ring.syncFields(null);
        return ring;
    }
}
