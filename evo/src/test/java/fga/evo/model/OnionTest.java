package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;

public class OnionTest {
    @Test
    public void testSyncFields_OneRing() {
        TestRing ring = new TestRing(0, 0);
        ring.initArea(Math.PI);
        TestOnion onion = new TestOnion();
        onion.addRing(ring);

        onion.syncFields();

        assertEquals(1, ring.getOuterRadius(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass(), 0.001);
        assertEquals(ring.getOuterRadius(), onion.getRadius(), 0.001);
        assertEquals(ring.getArea(), onion.getArea(), 0.001);
        assertEquals(ring.getMass(), onion.getMass(), 0.001);
    }
    @Test
    public void testSyncFields_TwoRings() {
        TestRing ring1 = new TestRing(0, 0);
        ring1.initOuterRadius(1);
        TestRing ring2 = new TestRing(0, 0);
        ring2.initOuterRadius(2);
        TestOnion onion = new TestOnion();
        onion.addRing(ring1);
        onion.addRing(ring2);

        onion.syncFields();

        assertEquals(Math.PI, ring1.getArea(), 0.001);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), ring1.getMass(), 0.001);
        assertEquals(3 * Math.PI, ring2.getArea(), 0.001);
        assertEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring2.getMass(), 0.001);
        assertEquals(ring2.getOuterRadius(), onion.getRadius(), 0.001);
        assertEquals(ring1.getArea() + ring2.getArea(), onion.getArea(), 0.001);
        assertEquals(ring1.getMass() + ring2.getMass(), onion.getMass(), 0.001);
    }

    public static class TestOnion extends Onion {
    }
}
