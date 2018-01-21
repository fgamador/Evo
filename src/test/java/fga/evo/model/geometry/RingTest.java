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
    public void testSyncFields_OuterRadius_NoInnerRing() {
        TestRing ring = new TestRing(1);

        ring.syncFields(null);

        assertEquals(Math.PI, ring.getArea(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
    }

    @Test
    public void testSyncFields_Area_InnerRing() {
        TestRing innerRing = new TestRing(1);
        innerRing.syncFields(null);
        TestRing ring = new TestRing(0);
        ring.setArea(3 * Math.PI);

        ring.syncFields(innerRing);

        assertEquals(2, ring.getOuterRadius(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
    }

    @Test
    public void testSyncFields_OuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1);
        innerRing.syncFields(null);
        TestRing ring = new TestRing(2);

        ring.syncFields(innerRing);

        assertEquals(3 * Math.PI, ring.getArea(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
    }

    @Test
    public void testSyncFields_ZeroOuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1);
        innerRing.syncFields(null);
        TestRing ring = new TestRing(0);

        ring.syncFields(innerRing);

        assertEquals(1, ring.getOuterRadius(), 0);
        assertEquals(0, ring.getArea(), 0);
        assertEquals(0, ring.getMass(), 0);
    }
}
