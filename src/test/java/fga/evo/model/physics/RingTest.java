package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import fga.evo.model.TestRing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RingTest extends EvoTest {
    @Test
    public void testSetArea() {
        TestRing ring = new TestRing(1, null);

        ring.setArea(Math.PI);

        assertEquals(Math.PI, ring.getArea(), 0.001);
        assertEquals(0, ring.getOuterRadius(), 0);
    }

    @Test
    public void testSetOuterRadius() {
        TestRing ring = new TestRing(1, null);

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
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
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
        TestRing innerRing = new TestRing(1, null);
        TestRing ring = new TestRing(0);
        ring.setArea(3 * Math.PI);

        ring.syncFields(innerRing);

        assertEquals(2, ring.getOuterRadius(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
    }

    @Test
    public void testSyncFields_OuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1, null);
        TestRing ring = new TestRing(2);

        ring.syncFields(innerRing);

        assertEquals(3 * Math.PI, ring.getArea(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
    }

    @Test
    public void testSyncFields_ZeroOuterRadius_InnerRing() {
        TestRing innerRing = new TestRing(1, null);
        TestRing ring = new TestRing(0);

        ring.syncFields(innerRing);

        assertEquals(1, ring.getOuterRadius(), 0);
        assertEquals(0, ring.getArea(), 0);
        assertEquals(0, ring.getMass(), 0);
    }
}
