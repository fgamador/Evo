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
        TestTissueRing.parameters.maxResizeFactor.revertToDefaultValue();
        TestTissueRing.parameters.minResizeFactor.revertToDefaultValue();
        TestTissueRing.parameters.maxGrowthRate_Old.revertToDefaultValue();
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

//    @Test
//    public void shrinkageYieldsExpectedEnergy_New() {
//        final double startArea = Math.PI;
//        TissueRing testSubject = new TestTissueRing(startArea);
//
//        final double resizeFactor = 0.9;
//        testSubject.requestResize_New(resizeFactor);
//
//        final double deltaArea = resizeFactor * startArea - startArea;
//        final double shrinkageYield = deltaArea * TestTissueRing.parameters.shrinkageYield.getValue();
//        assertTrue(shrinkageYield < 0);
//        assertEquals(shrinkageYield, testSubject.getIntendedEnergyConsumption(), Assert.DEFAULT_DELTA);
//    }
//
//    @Test
//    public void growthIsLimitedByMaxResizeFactor_New() {
//        TestTissueRing.parameters.maxResizeFactor.setValue(1.1);
//        final double startArea = Math.PI;
//        TissueRing testSubject = new TestTissueRing(startArea);
//
//        testSubject.requestResize_New(10);
//        testSubject.resize();
//
//        assertEquals(TestTissueRing.parameters.maxResizeFactor.getValue() * startArea, testSubject.getArea(), 0);
//    }
//
//    @Test
//    public void shrinkageIsLimitedByMinResizeFactor_New() {
//        TestTissueRing.parameters.minResizeFactor.setValue(0.1);
//        final double startArea = Math.PI;
//        TissueRing testSubject = new TestTissueRing(startArea);
//
//        testSubject.requestResize_New(0.01);
//        testSubject.resize();
//
//        assertEquals(TestTissueRing.parameters.minResizeFactor.getValue() * startArea, testSubject.getArea(), 0);
//    }
//
//    @Test
//    public void scalingResizeRequestScalesEnergyConsumption_New() {
//        TissueRing testSubject = new TestTissueRing(Math.PI);
//        testSubject.requestResize_New(10);
//        final double unscaledEnergy = testSubject.getIntendedEnergyConsumption();
//
//        testSubject.scaleResizeRequest(0.1);
//
//        assertEquals(unscaledEnergy * 0.1, testSubject.getIntendedEnergyConsumption(), 0);
//    }

    //-----------------

    @Test
    public void growsByRequestedAmount_New() {
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        final double resizeFactor = 2;
        testSubject.requestResize_New(resizeFactor);
        testSubject.resize();

        assertEquals(resizeFactor * startArea, testSubject.getArea(), 0);
    }

    @Test
    public void shrinksByRequestedAmount_New() {
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        final double resizeFactor = 0.5;
        testSubject.requestResize_New(resizeFactor);
        testSubject.resize();

        assertEquals(resizeFactor * startArea, testSubject.getArea(), 0);
    }

    // TODO need a notion of minimum area
    @Test
    public void canGrowFromZeroArea_New() {
        TissueRing testSubject = new TestTissueRing(0);

        testSubject.requestResize_New(2);
        testSubject.resize();

        assertEquals(1, testSubject.getArea(), 0);
    }

    @Test
    public void cannotShrinkBelowZeroArea_New() {
        TissueRing testSubject = new TestTissueRing(0.1);

        testSubject.requestResize_New(0.00001);
        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void growthUsesExpectedEnergy_New() {
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        final double resizeFactor = 1.1;
        testSubject.requestResize_New(resizeFactor);

        final double deltaArea = resizeFactor * startArea - startArea;
        final double growthCost = deltaArea * TestTissueRing.parameters.growthCost.getValue();
        assertTrue(growthCost > 0);
        assertEquals(growthCost, testSubject.getIntendedEnergyConsumption(), Assert.DEFAULT_DELTA);
    }

    @Test
    public void shrinkageYieldsExpectedEnergy_New() {
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        final double resizeFactor = 0.9;
        testSubject.requestResize_New(resizeFactor);

        final double deltaArea = resizeFactor * startArea - startArea;
        final double shrinkageYield = deltaArea * TestTissueRing.parameters.shrinkageYield.getValue();
        assertTrue(shrinkageYield < 0);
        assertEquals(shrinkageYield, testSubject.getIntendedEnergyConsumption(), Assert.DEFAULT_DELTA);
    }

    @Test
    public void growthIsLimitedByMaxResizeFactor_New() {
        TestTissueRing.parameters.maxResizeFactor.setValue(1.1);
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        testSubject.requestResize_New(10);
        testSubject.resize();

        assertEquals(TestTissueRing.parameters.maxResizeFactor.getValue() * startArea, testSubject.getArea(), 0);
    }

    @Test
    public void shrinkageIsLimitedByMinResizeFactor_New() {
        TestTissueRing.parameters.minResizeFactor.setValue(0.1);
        final double startArea = Math.PI;
        TissueRing testSubject = new TestTissueRing(startArea);

        testSubject.requestResize_New(0.01);
        testSubject.resize();

        assertEquals(TestTissueRing.parameters.minResizeFactor.getValue() * startArea, testSubject.getArea(), 0);
    }

    @Test
    public void scalingResizeRequestScalesEnergyConsumption_New() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_New(10);
        final double unscaledEnergy = testSubject.getIntendedEnergyConsumption();

        testSubject.scaleResizeRequest(0.1);

        assertEquals(unscaledEnergy * 0.1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    //-----------------

    @Test
    public void resizeWithoutRequestDoesNothing() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.resize();

        assertEquals(initialArea, testSubject.getArea(), 0);
        assertEquals(0, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void resizeIsIdempotent() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_New(2);

        testSubject.resize();
        final double areaAfterFirstResize = testSubject.getArea();
        testSubject.resize();

        assertEquals(areaAfterFirstResize, testSubject.getArea(), 0);
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

    //-----------------

    @Test
    public void growthUsesAvailableEnergy_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_Old(2);
        assertEquals(2, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void growthEnergyConsumptionIsLimitedByMaxGrowthRate_Old() {
        TestTissueRing.parameters.maxGrowthRate_Old.setValue(0.1);
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double maxDeltaArea = testSubject.getArea() * TestTissueRing.parameters.maxGrowthRate_Old.getValue();
        double maxGrowthEnergy = maxDeltaArea * TestTissueRing.parameters.growthCost.getValue();
        assertTrue(maxGrowthEnergy < 100);

        testSubject.requestResize_Old(100);

        assertEquals(maxGrowthEnergy, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void shrinkageYieldsRequestedEnergy_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_Old(-0.1);
        assertEquals(-0.1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void shrinkageEnergyYieldIsLimitedByAvailableArea_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();

        testSubject.requestResize_Old(-5);

        double energyFromShrinkingToZeroArea = initialArea * TestTissueRing.parameters.shrinkageYield.getValue();
        assertEquals(-energyFromShrinkingToZeroArea, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void scalingResizeRequestScalesEnergyConsumption_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);

        testSubject.requestResize_Old(10);
        testSubject.scaleResizeRequest(0.1);

        assertEquals(1, testSubject.getIntendedEnergyConsumption(), 0);
    }

    @Test
    public void resizeChangesArea_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        double initialArea = testSubject.getArea();
        testSubject.requestResize_Old(2);

        testSubject.resize();

        double expectedFinalArea = initialArea + 2 / TestTissueRing.parameters.growthCost.getValue();
        assertEquals(expectedFinalArea, testSubject.getArea(), 0);
    }

    @Test
    public void resizeIsIdempotent_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_Old(2);
        testSubject.resize();
        double areaAfterFirstResize = testSubject.getArea();

        testSubject.resize();

        assertEquals(areaAfterFirstResize, testSubject.getArea(), 0);
    }

    @Test
    public void resizeWillNotShrinkBelowZeroArea_Old() {
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_Old(-5);

        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    @Test
    public void shrinkingWithZeroShrinkageYieldParameterShrinksToZeroArea_Old() {
        TestTissueRing.parameters.shrinkageYield.setValue(0);
        TissueRing testSubject = new TestTissueRing(Math.PI);
        testSubject.requestResize_Old(-0.01);

        testSubject.resize();

        assertEquals(0, testSubject.getArea(), 0);
    }

    private static class TestTissueRing extends TissueRing {
        static TissueRingParameters parameters = new TissueRingParameters();

        static {
            parameters.density = new DoubleParameter(0.5);
            parameters.growthCost = new DoubleParameter(0.1);
            parameters.maintenanceCost = new DoubleParameter(0.05);
            parameters.shrinkageYield = new DoubleParameter(0.05);
            parameters.maxResizeFactor = new DoubleParameter(100);
            parameters.minResizeFactor = new DoubleParameter(0.01);
            parameters.maxGrowthRate_Old = new DoubleParameter(100);
            parameters.maxShrinkRate_Old = new DoubleParameter(1);
            parameters.decayRate = new DoubleParameter(0.1);
        }

        TestTissueRing(double area) {
            super(parameters, area);
            setRadiiBasedOnArea(0);
        }
    }
}
