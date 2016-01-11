package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Util.sqr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest {
    @After
    public void tearDown() {
        TestRing.parameters.maxGrowthRate.revertToDefaultValue();
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
        TestRing.parameters.maxGrowthRate.setValue(0.1);
        TestRing ring = new TestRing(1, null);
        double maxDeltaArea = ring.getArea() * TestRing.parameters.maxGrowthRate.getValue();
        double maxGrowthEnergy = maxDeltaArea * TestRing.parameters.growthCost.getValue();

        ring.requestResize(100);

        assertTrue(maxGrowthEnergy < 100);
        assertEquals(maxGrowthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_Shrink() {
        TestRing ring = new TestRing(1, null);

        ring.requestResize(-0.1);

        assertEquals(-0.1, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_NotBelowZero() {
        TestRing ring = new TestRing(1, null);

        ring.requestResize(-5);

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
        ring.requestResize(2);

        ring.resize();

        assertEquals(Math.PI + 2 / TestRing.parameters.growthCost.getValue(), ring.getArea(), 0);
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
        ring.requestResize(2);
        ring.resize();

        ring.resize();

        assertEquals(Math.PI + 2 / TestRing.parameters.growthCost.getValue(), ring.getArea(), 0);
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
