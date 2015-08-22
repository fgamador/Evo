package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TissueRingTest {
    @Test
    public void testNewAsInnermostRing() {
        TestRing ring = new TestRing(1, 0);
        assertEquals(Math.PI, ring.getArea(), 0);
        assertEquals(ring.getArea() / 2, ring.getMass(), 0);

        ring = new TestRing(2, 0);
        assertEquals(Math.PI * 4, ring.getArea(), 0);
        assertEquals(ring.getArea() / 2, ring.getMass(), 0);
    }

    @Test
    public void testNewAsOuterRing() {
        TestRing ring = new TestRing(1, 0.5);
        assertEquals(Math.PI - 0.5, ring.getArea(), 0);
        assertEquals(ring.getArea() / 2, ring.getMass(), 0);
    }

    @Test
    public void testRequestResize() {
        TestRing ring = new TestRing(1, 0);

        ring.requestResize(5);

        assertEquals(5 * TestRing.parameters.getGrowthCost(), ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testScaleResizeRequest() {
        TestRing ring = new TestRing(1, 0);
        ring.requestResize(10);

        ring.scaleResizeRequest(0.5);

        assertEquals(5 * TestRing.parameters.getGrowthCost(), ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testResize() {
        TestRing ring = new TestRing(1, 0);
        ring.requestResize(5);

        ring.resize();

        assertEquals(Math.PI + 5, ring.getArea(), 0);
    }

    @Test
    public void testResize_ClearRequest() {
        TestRing ring = new TestRing(1, 0);
        ring.requestResize(5);
        ring.resize();

        ring.resize();

        assertEquals(Math.PI + 5, ring.getArea(), 0);
    }

    // TODO test shrinkage

    @Test
    public void testUpdateFromArea() {
        TestRing ring = new TestRing(1, 0);
        ring.requestResize(3 * Math.PI);
        ring.resize();

        ring.updateFromArea(0);

        assertEquals(2, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testUpdateFromAreaAsOuterRing() {
        TestRing innerRing = new TestRing(1, 0);
        TestRing ring = new TestRing(2, innerRing.getArea());
        ring.requestResize(5 * Math.PI);
        ring.resize();

        ring.updateFromArea(innerRing.getOuterRadius());

        assertEquals(3, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testGetMaintenanceEnergy() {
        TestRing ring = new TestRing(3, 0);
        assertEquals(Math.PI * 9 * TestRing.parameters.getMaintenanceCost(), ring.getMaintenanceEnergy(), 0);
    }

    public static class TestRing extends TissueRing {
        public static final Parameters parameters = new Parameters();
        static {
            parameters.setTissueDensity(0.5);
            parameters.setGrowthCost(0.1);
            parameters.setMaintenanceCost(0.05);
            parameters.setShrinkageYield(0.05);
        }

        public TestRing(double outerRadius, double innerArea) {
            super(parameters, outerRadius, innerArea);
        }
    }
}
