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
    public void testGrowArea() {
        TestRing ring = new TestRing(1, 0);

        ring.growArea(2);

        assertEquals(Math.PI + 2 / TestRing.parameters.getGrowthCost(), ring.getArea(), 0);
    }

//    @Test
//    public void testGrowArea_Negative() {
//        TestRing ring = new TestRing(100, 0);
//
//        double cost = ring.growArea(-1);
//
//        assertEquals(-1, cost, 0); // TODO
//        assertEquals(Math.PI + 2 / TestRing.parameters.getShrinkageYield(), ring.getArea(), 0);
//    }

    @Test
    public void testUpdateFromArea() {
        TestRing ring = new TestRing(1, 0);
        ring.growArea(2);

        ring.updateFromArea(0);

        assertEquals(2.7, ring.getOuterRadius(), 0.1);
        assertEquals(ring.getArea() * TestRing.parameters.getTissueDensity(), ring.getMass(), 0);
    }

    @Test
    public void testUpdateFromAreaAsOuterRing() {
        TestRing innerRing = new TestRing(0.5, 0);
        TestRing ring = new TestRing(1, innerRing.getArea());
        ring.growArea(2);

        ring.updateFromArea(innerRing.getOuterRadius());

        assertEquals(2.7, ring.getOuterRadius(), 0.1);
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
