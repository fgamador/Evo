package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest {
    // TODO do we really need to test the combo of resize and updateFromArea?
    @Test
    public void testUpdateFromArea() {
        TestRing ring = new TestRing(1, null);
        double newOuterRadius = 2;
        double newArea = Math.PI * sqr(newOuterRadius);
        ring.requestResize((newArea - ring.getArea()) * TestRing.parameters.growthCost.getValue());
        ring.resize();

        ring.updateFromArea(0);

        assertEquals(newOuterRadius, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.density.getValue(), ring.getMass(), 0);
    }

    // TODO do we really need to test the combo of resize and updateFromArea?
    @Test
    public void testUpdateFromAreaAsOuterRing() {
        TestRing innerRing = new TestRing(1, null);
        TestRing ring = new TestRing(2, innerRing);
        double newOuterRadius = 3;
        double newArea = Math.PI * sqr(newOuterRadius);
        ring.requestResize((newArea - (innerRing.getArea() + ring.getArea())) * TestRing.parameters.growthCost.getValue());
        ring.resize();

        ring.updateFromArea(innerRing.getOuterRadius());

        assertEquals(3, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.density.getValue(), ring.getMass(), 0);
    }

    @Test
    public void testRequestResize_Grow() {
        TestRing ring = new TestRing(1, null);

        double growthEnergy = 2;
        ring.requestResize(growthEnergy);

        assertEquals(growthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_MaxGrowthRate() {
        double defaultMaxGrowthRate = TestRing.parameters.maxGrowthRate.getValue();
        try {
            TestRing.parameters.maxGrowthRate.setValue(0.1);
            TestRing ring = new TestRing(1, null);
            double maxDeltaArea = ring.getArea() * TestRing.parameters.maxGrowthRate.getValue();
            double maxGrowthEnergy = maxDeltaArea * TestRing.parameters.growthCost.getValue();
            double growthEnergy = 100;

            ring.requestResize(growthEnergy);

            assertTrue(maxGrowthEnergy < growthEnergy);
            assertEquals(maxGrowthEnergy, ring.getRequestedEnergy(), 0);
        } finally {
            TestRing.parameters.maxGrowthRate.setValue(defaultMaxGrowthRate);
        }
    }

    @Test
    public void testRequestResize_Shrink() {
        TestRing ring = new TestRing(1, null);

        double growthEnergy = -0.1;
        ring.requestResize(growthEnergy);

        assertEquals(growthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_NotBelowZero() {
        TestRing ring = new TestRing(1, null);

        double growthEnergy = -5;
        ring.requestResize(growthEnergy);

        assertEquals(-Math.PI * TestRing.parameters.shrinkageYield.getValue(), ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testScaleResizeRequest() {
        TestRing ring = new TestRing(1, null);

        ring.requestResize(10);
        ring.scaleResizeRequest(0.1);

        assertEquals(1, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testResize() {
        TestRing ring = new TestRing(1, null);
        double growthEnergy = 2;
        ring.requestResize(growthEnergy);

        ring.resize();

        assertEquals(Math.PI + growthEnergy / TestRing.parameters.growthCost.getValue(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NoRequest() {
        TestRing ring = new TestRing(1, null);

        ring.resize();

        assertEquals(Math.PI, ring.getArea(), 0);
    }

    @Test
    public void testResize_DoNotRetainRequest() {
        TestRing ring = new TestRing(1, null);
        double growthEnergy = 2;
        ring.requestResize(growthEnergy);
        ring.resize();

        ring.resize();

        assertEquals(Math.PI + growthEnergy / TestRing.parameters.growthCost.getValue(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NotBelowZero() {
        TestRing ring = new TestRing(1, null);
        ring.requestResize(-5);

        ring.resize();

        assertEquals(0, ring.getArea(), 0);
    }

    @Test
    public void testGetMaintenanceEnergy() {
        TestRing ring = new TestRing(3, null);
        assertEquals(Math.PI * 9 * TestRing.parameters.maintenanceCost.getValue(), ring.getMaintenanceEnergy(), 0);
    }

    @Test
    public void testDecay() {
        TestRing ring = new TestRing(1, null);
        double initialArea = ring.getArea();

        ring.decay();

        double newArea = initialArea * (1 - TestRing.parameters.decayRate.getValue());
        assertEquals(newArea, ring.getArea(), 0.001);
    }

    public static class TestRing extends TissueRing {
        public static TissueRingParameters parameters = new TissueRingParameters();
        static {
            parameters.density = new DoubleParameter(0.5);
            parameters.growthCost = new DoubleParameter(0.1);
            parameters.maintenanceCost = new DoubleParameter(0.05);
            parameters.shrinkageYield = new DoubleParameter(0.05);
            parameters.maxGrowthRate = new DoubleParameter(100);
            parameters.decayRate = new DoubleParameter(0.1);
        }

        public TestRing(double outerRadius, TestRing innerRing) {
            super(parameters, outerRadius);
            syncFields(innerRing);
        }
    }
}
