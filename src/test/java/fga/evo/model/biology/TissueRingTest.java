package fga.evo.model.biology;

import fga.evo.model.EvoTest;
import fga.evo.model.util.DoubleParameter;
import org.junit.After;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TissueRingTest extends EvoTest {
    @After
    public void restoreParameters() {
        TestTissueRing.parameters.maxGrowthRate.revertToDefaultValue();
        TestTissueRing.parameters.maxShrinkRate.revertToDefaultValue();
    }

    @Test
    public void growsByRequestedAmount() {
        TissueRing subject = new TestTissueRing(Math.PI);

        subject.requestResize(0.5);
        subject.resize();

        assertApproxEquals(Math.PI + 0.5, subject.getArea());
    }

    @Test
    public void shrinksByRequestedAmount() {
        TissueRing subject = new TestTissueRing(Math.PI);

        subject.requestResize(-0.5);
        subject.resize();

        assertApproxEquals(Math.PI - 0.5, subject.getArea());
    }

    @Test
    public void cannotShrinkBelowZeroArea() {
        TestTissueRing.parameters.maxShrinkRate.setValue(1);
        TissueRing subject = new TestTissueRing(1);

        subject.requestResize(-2);
        subject.resize();

        assertEquals(0, subject.getArea(), 0);
    }

    @Test
    public void growthUsesExpectedEnergy() {
        TissueRing subject = new TestTissueRing(Math.PI);

        subject.requestResize(1.1);

        final double growthCost = 1.1 * TestTissueRing.parameters.growthCost.getValue();
        assertTrue(growthCost > 0);
        assertApproxEquals(growthCost, subject.getIntendedEnergyConsumption());
    }

    @Test
    public void shrinkageYieldsExpectedEnergy() {
        TissueRing subject = new TestTissueRing(Math.PI);

        final double deltaArea = -0.9;
        subject.requestResize(deltaArea);

        final double shrinkageYield = deltaArea * TestTissueRing.parameters.shrinkageYield.getValue();
        assertTrue(shrinkageYield < 0);
        assertApproxEquals(shrinkageYield, subject.getIntendedEnergyConsumption());
    }

    @Test
    public void growthIsLimitedByMaxGrowthRate() {
        TestTissueRing.parameters.maxGrowthRate.setValue(0.1);
        final double startArea = Math.PI;
        TissueRing subject = new TestTissueRing(startArea);

        subject.requestResize(10);
        subject.resize();

        assertApproxEquals((1 + TestTissueRing.parameters.maxGrowthRate.getValue()) * startArea, subject.getArea());
    }

    @Test
    public void shrinkageIsLimitedByMaxShrinkRate() {
        TestTissueRing.parameters.maxShrinkRate.setValue(0.1);
        final double startArea = Math.PI;
        TissueRing subject = new TestTissueRing(startArea);

        subject.requestResize(-2);
        subject.resize();

        assertApproxEquals((1 - TestTissueRing.parameters.maxShrinkRate.getValue()) * startArea, subject.getArea());
    }

    @Test
    public void canGrowFromZeroArea() {
        TestTissueRing.parameters.maxGrowthRate.setValue(10);
        TissueRing subject = new TestTissueRing(0);

        subject.requestResize(1);
        subject.resize();

        assertApproxEquals(1, subject.getArea());
    }

    @Test
    public void scalingResizeRequestScalesEnergyConsumption() {
        TissueRing subject = new TestTissueRing(Math.PI);
        subject.requestResize(10);
        final double unscaledEnergy = subject.getIntendedEnergyConsumption();

        subject.scaleResizeRequest(0.1);

        assertApproxEquals(unscaledEnergy * 0.1, subject.getIntendedEnergyConsumption());
    }

    @Test
    public void resizeIsIdempotent() {
        TissueRing subject = new TestTissueRing(Math.PI);
        subject.requestResize(2);

        subject.resize();
        final double areaAfterFirstResize = subject.getArea();
        subject.resize();

        assertApproxEquals(areaAfterFirstResize, subject.getArea());
    }

    @Test
    public void resizeWithoutRequestDoesNothing() {
        TissueRing subject = new TestTissueRing(Math.PI);
        final double initialArea = subject.getArea();

        subject.resize();

        assertEquals(initialArea, subject.getArea(), 0);
        assertEquals(0, subject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void maintenanceEnergyDependsOnMaintenanceCost() {
        TissueRing subject = new TestTissueRing(Math.PI * 9);
        final double expectedMaintenanceEnergy = subject.getArea() * TestTissueRing.parameters.maintenanceCost.getValue();
        assertApproxEquals(expectedMaintenanceEnergy, subject.getMaintenanceEnergy());
    }

    @Test
    public void decayUsesExpectedCalculation() {
        TissueRing subject = new TestTissueRing(Math.PI);
        final double initialArea = subject.getArea();

        subject.decay();

        final double expectedFinalArea = initialArea * (1 - TestTissueRing.parameters.decayRate.getValue());
        assertApproxEquals(expectedFinalArea, subject.getArea());
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
