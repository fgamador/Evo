package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import fga.evo.model.util.DoubleParameter;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest extends EvoTest {
    @After
    public void tearDown() {
        TestTissueRing.parameters.maxGrowthRate.revertToDefaultValue();
        TestTissueRing.parameters.shrinkageYield.revertToDefaultValue();
    }

    @Test
    public void maintenanceEnergyDependsOnMaintenanceCost() {
        TissueRing testSubject = new TestTissueRing(Math.PI * 9);
        double expectedMaintenanceEnergy = testSubject.getArea() * TestTissueRing.parameters.maintenanceCost.getValue();
        assertEquals(expectedMaintenanceEnergy, testSubject.getMaintenanceEnergy(), 0);
    }

    @Test
    public void growthUsesAvailableEnergy() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(2);
        assertEquals(2, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void growthEnergyConsumptionIsLimitedByMaxGrowthRate() {
        TestTissueRing.parameters.maxGrowthRate.setValue(0.1);
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double maxDeltaArea = testSubject.getArea() * TestTissueRing.parameters.maxGrowthRate.getValue();
        double maxGrowthEnergy = maxDeltaArea * TestTissueRing.parameters.growthCost.getValue();
        assertTrue(maxGrowthEnergy < 100);

        testSubject.requestResize(100);

        assertEquals(maxGrowthEnergy, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void shrinkageYieldsRequestedEnergy() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(-0.1);
        assertEquals(-0.1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void shrinkageEnergyYieldIsLimitedByAvailableArea() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.requestResize(-5);

        double energyFromShrinkingToZeroArea = initialArea * TestTissueRing.parameters.shrinkageYield.getValue();
        assertEquals(-energyFromShrinkingToZeroArea, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void scalingResizeRequestScalesEnergyConsumption() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        testSubject.requestResize(10);
        testSubject.scaleResizeRequest(0.1);

        assertEquals(1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void resizeChangesArea() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();
        testSubject.requestResize(2);

        testSubject.resize();

        double expectedFinalArea = initialArea + 2 / TestTissueRing.parameters.growthCost.getValue();
        assertEquals(expectedFinalArea, testSubject.getArea(), 0);
    }

    @Test
    public void resizeWithoutRequestDoesNothing() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.resize();

        assertEquals(initialArea, testSubject.getArea(), 0);
    }

    @Test
    public void resizeIsIdempotent() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(2);
        testSubject.resize();
        double areaAfterFirstResize = testSubject.getArea();

        testSubject.resize();

        assertEquals(areaAfterFirstResize, testSubject.getArea(), 0);
    }

    @Test
    public void resizeWillNotShrinkBelowZeroArea() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(-5);

        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void shrinkingWithZeroShrinkageYieldParameterShrinksToZeroArea() {
        TestTissueRing.parameters.shrinkageYield.setValue(0);
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(-0.01);

        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void decayUsesExpectedCalculation() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.decay();

        double expectedFinalArea = initialArea * (1 - TestTissueRing.parameters.decayRate.getValue());
        assertEquals(expectedFinalArea, testSubject.getArea(), 0.001);
    }

    private static class TestTissueRing extends TissueRing {
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

        public TestTissueRing(double area) {
            super(parameters, area);
            syncFields(null);
        }

        public TestTissueRing(double area, TestTissueRing innerRing) {
            super(parameters, area);
            syncFields(innerRing);
        }

        public TestTissueRing(double outerRadius, TestTissueRing innerRing, boolean marker) {
            super(parameters, outerRadius, false);
            syncFields(innerRing);
        }
    }
}
