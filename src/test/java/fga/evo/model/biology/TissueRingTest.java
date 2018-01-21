package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.TissueRing;
import fga.evo.model.biology.TissueRingParameters;
import fga.evo.model.util.DoubleParameter;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest extends EvoTest {
    @After
    public void tearDown() {
        TestTissueRing.parameters.maxGrowthRate.revertToDefaultValue();
    }

    @Test
    public void testRequestResize_Grow() {
        TestTissueRing ring = new TestTissueRing(1, null);

        double growthEnergy = 2;
        ring.requestResize(growthEnergy);

        assertEquals(growthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_MaxGrowthRate() {
        TestTissueRing.parameters.maxGrowthRate.setValue(0.1);
        TestTissueRing ring = new TestTissueRing(1, null);
        double maxDeltaArea = ring.getArea() * TestTissueRing.parameters.maxGrowthRate.getValue();
        double maxGrowthEnergy = maxDeltaArea * TestTissueRing.parameters.growthCost.getValue();

        ring.requestResize(100);

        assertTrue(maxGrowthEnergy < 100);
        assertEquals(maxGrowthEnergy, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_Shrink() {
        TestTissueRing ring = new TestTissueRing(1, null);

        ring.requestResize(-0.1);

        assertEquals(-0.1, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testRequestResize_NotBelowZero() {
        TestTissueRing ring = new TestTissueRing(1, null);

        ring.requestResize(-5);

        assertEquals(-Math.PI * TestTissueRing.parameters.shrinkageYield.getValue(), ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testScaleResizeRequest() {
        TestTissueRing ring = new TestTissueRing(1, null);

        ring.requestResize(10);
        ring.scaleResizeRequest(0.1);

        assertEquals(1, ring.getRequestedEnergy(), 0);
    }

    @Test
    public void testResize() {
        TestTissueRing ring = new TestTissueRing(1, null);
        ring.requestResize(2);

        ring.resize();

        assertEquals(Math.PI + 2 / TestTissueRing.parameters.growthCost.getValue(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NoRequest() {
        TestTissueRing ring = new TestTissueRing(1, null);

        ring.resize();

        assertEquals(Math.PI, ring.getArea(), 0);
    }

    @Test
    public void testResize_DoNotRetainRequest() {
        TestTissueRing ring = new TestTissueRing(1, null);
        ring.requestResize(2);
        ring.resize();

        ring.resize();

        assertEquals(Math.PI + 2 / TestTissueRing.parameters.growthCost.getValue(), ring.getArea(), 0);
    }

    @Test
    public void testResize_NotBelowZero() {
        TestTissueRing ring = new TestTissueRing(1, null);
        ring.requestResize(-5);

        ring.resize();

        assertEquals(0, ring.getArea(), 0);
    }

    @Test
    public void testGetMaintenanceEnergy() {
        TestTissueRing ring = new TestTissueRing(3, null);
        assertEquals(Math.PI * 9 * TestTissueRing.parameters.maintenanceCost.getValue(), ring.getMaintenanceEnergy(), 0);
    }

    @Test
    public void testDecay() {
        TestTissueRing ring = new TestTissueRing(1, null);
        double initialArea = ring.getArea();

        ring.decay();

        double newArea = initialArea * (1 - TestTissueRing.parameters.decayRate.getValue());
        assertEquals(newArea, ring.getArea(), 0.001);
    }

    public static class TestTissueRing extends TissueRing {
        public static TissueRingParameters parameters = new TissueRingParameters();

        static {
            parameters.density = new DoubleParameter(0.5);
            parameters.growthCost = new DoubleParameter(0.1);
            parameters.maintenanceCost = new DoubleParameter(0.05);
            parameters.shrinkageYield = new DoubleParameter(0.05);
            parameters.maxGrowthRate = new DoubleParameter(100);
            parameters.maxShrinkRate = new DoubleParameter(1);
            parameters.decayRate = new DoubleParameter(0.1);
        }

        public TestTissueRing(double outerRadius, TestTissueRing innerRing) {
            super(parameters, outerRadius);
            syncFields(innerRing);
        }
    }
}
