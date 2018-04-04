package fga.evo.model.biology;

import fga.evo.model.Assert;
import fga.evo.model.EvoTest;
import fga.evo.model.util.DoubleParameter;
import org.junit.After;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest extends EvoTest {
    @After
    public void tearDown() {
        TestTissueRing.parameters.maxGrowthRate.revertToDefaultValue();
        TestTissueRing.parameters.shrinkageYield.revertToDefaultValue();
    }

    @Test
    public void growsByRequestedAmount() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        testSubject.requestResize(0.5);
        testSubject.resize();

        assertApproxEquals(Math.PI + 0.5, testSubject.getArea());
    }

    @Test
    public void shrinksByRequestedAmount() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        testSubject.requestResize(-0.5);
        testSubject.resize();

        assertApproxEquals(Math.PI - 0.5, testSubject.getArea());
    }

    @Test
    public void cannotShrinkBelowZeroArea() {
        TestTissueRing.parameters.maxShrinkRate = new DoubleParameter(1);
        TissueRing testSubject = new TestTissueRing(1);

        testSubject.requestResize(-2);
        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void growthUsesExpectedEnergy() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        testSubject.requestResize(1.1);

        final double growthCost = 1.1 * TestTissueRing.parameters.growthCost.getValue();
        assertTrue(growthCost > 0);
        assertApproxEquals(growthCost, testSubject.getIntendedEnergyConsumption());
    }

    @Test
    public void shrinkageYieldsExpectedEnergy() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        final double deltaArea = -0.9;
        testSubject.requestResize(deltaArea);

        final double shrinkageYield = deltaArea * TestTissueRing.parameters.shrinkageYield.getValue();
        assertTrue(shrinkageYield < 0);
        assertApproxEquals(shrinkageYield, testSubject.getIntendedEnergyConsumption());
    }

    @Test
    public void growthIsLimitedByMaxGrowthRate() {
        TestTissueRing.parameters.maxGrowthRate.setValue(0.1);
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        testSubject.requestResize(10);
        testSubject.resize();

        assertApproxEquals((1 + TestTissueRing.parameters.maxGrowthRate.getValue()) * startArea, testSubject.getArea());
    }

    @Test
    public void shrinkageIsLimitedByMaxShrinkRate() {
        TestTissueRing.parameters.maxShrinkRate.setValue(0.1);
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        testSubject.requestResize(-2);
        testSubject.resize();

        assertApproxEquals((1 - TestTissueRing.parameters.maxShrinkRate.getValue()) * startArea, testSubject.getArea());
    }

    @Test
    public void canGrowFromZeroArea() {
        TestTissueRing.parameters.maxGrowthRate.setValue(10);
        TissueRing testSubject = new TestTissueRing(0);

        testSubject.requestResize(1);
        testSubject.resize();

        assertApproxEquals(1, testSubject.getArea());
    }

    @Test
    public void scalingResizeRequestScalesEnergyConsumption() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(10);
        final double unscaledEnergy = testSubject.getIntendedEnergyConsumption();

        testSubject.scaleResizeRequest(0.1);

        assertEquals(unscaledEnergy * 0.1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void resizeIsIdempotent() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize(2);

        testSubject.resize();
        final double areaAfterFirstResize = testSubject.getArea();
        testSubject.resize();

        assertEquals(areaAfterFirstResize, testSubject.getArea(), 0);
    }

    @Test
    public void resizeWithoutRequestDoesNothing() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.resize();

        assertEquals(initialArea, testSubject.getArea(), 0);
        assertEquals(0, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void maintenanceEnergyDependsOnMaintenanceCost() {
        TissueRing testSubject = new TestTissueRing(Math.PI * 9);
        double expectedMaintenanceEnergy = testSubject.getArea() * TestTissueRing.parameters.maintenanceCost.getValue();
        assertEquals(expectedMaintenanceEnergy, testSubject.getMaintenanceEnergy(), 0);
    }

    @Test
    public void decayUsesExpectedCalculation() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        final double initialArea = testSubject.getArea();

        testSubject.decay();

        final double expectedFinalArea = initialArea * (1 - TestTissueRing.parameters.decayRate.getValue());
        assertEquals(expectedFinalArea, testSubject.getArea(), 0.001);
    }

    private static class TestTissueRing extends TissueRing {
        static TissueRingParameters parameters = new TissueRingParameters();

        static {
            parameters.density = new DoubleParameter(0.5);
            parameters.growthCost = new DoubleParameter(0.1);
            parameters.maintenanceCost = new DoubleParameter(0.05);
            parameters.shrinkageYield = new DoubleParameter(0.05);
            parameters.maxGrowthRate = new DoubleParameter(100);
            parameters.maxShrinkRate = new DoubleParameter(1);
            parameters.decayRate = new DoubleParameter(0.1);
        }

        TestTissueRing(double area) {
            super(parameters, area);
            setRadiiBasedOnArea(0);
        }
    }
}
