package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RingTest {
    @Test
    public void testNewAsInnermostRing() {
        double radius = 2;
        TestRing ring = new TestRing(radius);
        ring.syncFields(null);

        assertEquals(Math.PI * sqr(radius), ring.getArea(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.density.getValue(), ring.getMass(), 0);
    }

    @Test
    public void testNewAsOuterRing() {
        TestRing innerRing = new TestRing(1);
        TestRing ring = new TestRing(2);
        innerRing.syncFields(null);
        ring.syncFields(innerRing);

        assertEquals(3 * Math.PI, ring.getArea(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.density.getValue(), ring.getMass(), 0);
    }

    @Test
    public void testSetArea() {
        TestRing ring = new TestRing(1);
        ring.syncFields(null);

        ring.setArea(Math.PI);

        assertEquals(Math.PI, ring.getArea(), 0.001);
        assertEquals(0, ring.getOuterRadius(), 0);
    }

    @Test
    public void testSetOuterRadius() {
        TestRing ring = new TestRing(1);
        ring.syncFields(null);

        ring.setOuterRadius(2);

        assertEquals(0, ring.getArea(), 0);
        assertEquals(2, ring.getOuterRadius(), 0);
    }

    @Test
    public void testSyncFields_Area_NoInnerRing() {
        TestRing ring = new TestRing(0);
        ring.setArea(Math.PI);

        ring.syncFields(null);

        assertEquals(1, ring.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.01);
    }

    @Test
    public void testSyncFields_Area_InnerRing() {
        TestRing innerRing = new TestRing(1);
        TestRing ring = new TestRing(0);
        ring.setArea(3 * Math.PI);

        ring.syncFields(innerRing);

        assertEquals(2, ring.getOuterRadius(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.01);
    }

    @Test
    public void testSyncFields_OuterRadius_NoInnerRing() {
        TestRing ring = new TestRing(0);
        ring.setOuterRadius(1);

        ring.syncFields(null);

        assertEquals(Math.PI, ring.getArea(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.01);
    }

    @Test
    public void testSyncFields_OuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1);
        innerRing.syncFields(null);
        TestRing ring = new TestRing(0);
        ring.setOuterRadius(2);

        ring.syncFields(innerRing);

        assertEquals(3 * Math.PI, ring.getArea(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.01);
    }

    @Test
    public void testSyncFields_ZeroOuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1);
        innerRing.syncFields(null);
        TestRing ring = new TestRing(0);

        ring.syncFields(innerRing);

        assertEquals(1, ring.getOuterRadius(), 0.001);
        assertEquals(0, ring.getArea(), 0.001);
        assertEquals(0, ring.getMass(), 0.001);
    }
}
