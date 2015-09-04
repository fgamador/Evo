package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;

public class TissueRingTest {
    @Test
    public void testNewAsInnermostRing() {
        final double radius = 2;
        TestRing ring = new TestRing(radius, 0);

        assertEquals(Math.PI * sqr(radius), ring.getArea(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testNewAsOuterRing() {
        final double innerArea = 0.5;
        TestRing ring = new TestRing(1, innerArea);

        assertEquals(Math.PI - innerArea, ring.getArea(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testRequestResize_Grow() {
        TestRing ring = new TestRing(1, 0);

        final double growthEnergy = 2;
        ring.requestResize(growthEnergy);

        assertEquals(growthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_Shrink() {
        TestRing ring = new TestRing(1, 0);

        final double growthEnergy = -0.1;
        ring.requestResize(growthEnergy);

        assertEquals(growthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_NotBelowZero() {
        TestRing ring = new TestRing(1, 0);

        final double growthEnergy = -5;
        ring.requestResize(growthEnergy);

        assertEquals(-Math.PI * TestRing.parameters.getShrinkageYield(), ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testScaleResizeRequest() {
        TestRing ring = new TestRing(1, 0);

        ring.requestResize(100);
        ring.scaleResizeRequest(0.1);

        assertEquals(10, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testResize() {
        TestRing ring = new TestRing(1, 0);
        final double growthEnergy = 2;
        ring.requestResize(growthEnergy);

        ring.resize();

        assertEquals(Math.PI + growthEnergy / TestRing.parameters.getGrowthCost(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NoRequest() {
        TestRing ring = new TestRing(1, 0);

        ring.resize();

        assertEquals(Math.PI, ring.getArea(), 0);
    }

    @Test
    public void testResize_DoNotRetainRequest() {
        TestRing ring = new TestRing(1, 0);
        final double growthEnergy = 2;
        ring.requestResize(growthEnergy);
        ring.resize();

        ring.resize();

        assertEquals(Math.PI + growthEnergy / TestRing.parameters.getGrowthCost(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NotBelowZero() {
        TestRing ring = new TestRing(1, 0);
        ring.requestResize(-5);

        ring.resize();

        assertEquals(0, ring.getArea(), 0);
    }

    @Test
    public void testUpdateFromArea() {
        TestRing ring = new TestRing(1, 0);
        final double newOuterRadius = 2;
        final double newArea = Math.PI * sqr(newOuterRadius);
        ring.requestResize((newArea - ring.getArea()) * TestRing.parameters.getGrowthCost());
        ring.resize();

        ring.updateFromArea(0);

        assertEquals(newOuterRadius, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testUpdateFromAreaAsOuterRing() {
        TestRing innerRing = new TestRing(1, 0);
        TestRing ring = new TestRing(2, innerRing.getArea());
        final double newOuterRadius = 3;
        final double newArea = Math.PI * sqr(newOuterRadius);
        ring.requestResize((newArea - (innerRing.getArea() + ring.getArea())) * TestRing.parameters.getGrowthCost());
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
