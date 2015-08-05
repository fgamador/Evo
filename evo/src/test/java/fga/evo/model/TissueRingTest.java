package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TissueRingTest {
    @Test
    public void testUpdateFromOuterRadius() {
        TestRing ring = new TestRing(1);
        assertEquals(Math.PI, ring.getArea(), 0);
        assertEquals(Math.PI / 2, ring.getMass(), 0);

        ring = new TestRing(2);
        assertEquals(Math.PI * 4, ring.getArea(), 0);
        assertEquals(Math.PI * 2, ring.getMass(), 0);
    }

    @Test
    public void testGrowArea() {
        TestRing ring = new TestRing(1);

        ring.growArea(2);

        assertEquals(Math.PI + 2 / TestRing.parameters.getGrowthCost(), ring.getArea(), 0);
    }

    @Test
    public void testUpdateFromArea() {
        TestRing ring = new TestRing(1);
        ring.growArea(2);

        ring.updateFromArea();

        //assertEquals(2, ring.getOuterRadius(), 0);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testGetMaintenanceEnergy() {
        TestRing ring = new TestRing(3);
        assertEquals(Math.PI * 9 * TestRing.parameters.getMaintenanceCost(), ring.getMaintenanceEnergy(), 0);
    }

    public static class TestRing extends TissueRing {
        public static final Parameters parameters = new Parameters();
        static {
            parameters.setTissueDensity(0.5);
            parameters.setGrowthCost(0.1);
            parameters.setMaintenanceCost(0.05);
            //parameters.setShrinkageYield(0);
        }

        public TestRing(double outerRadius) {
            super(parameters, outerRadius);
        }
    }
}
