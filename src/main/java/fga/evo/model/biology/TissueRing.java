package fga.evo.model.biology;

import fga.evo.model.geometry.Ring;
import fga.evo.model.util.DoubleParameter;

/**
 * Base class for the cell's rings of tissue.
 */
public class TissueRing extends Ring {
    private TissueRingParameters parameters;
    private double intendedEnergyConsumption;
    private double deltaArea;

    protected TissueRing(TissueRingParameters parameters, double area) {
        super(parameters, area);
        this.parameters = parameters;
    }

    public void requestResize(double deltaArea) {
        this.deltaArea = deltaArea;
        intendedEnergyConsumption = calcIntendedEnergyConsumption(this.deltaArea);
    }

    // ----------------

    public void requestResize_New(double factor) {
        deltaArea = calcDeltaArea_New(factor);
        intendedEnergyConsumption = calcIntendedEnergyConsumption(deltaArea);
    }

    private double calcDeltaArea_New(double factor) {
        final double maxFactor = parameters.maxResizeFactor.getValue();
        final double minFactor = parameters.minResizeFactor.getValue();
        final double boundedFactor = Math.max(minFactor, Math.min(maxFactor, factor));
        // TODO need a notion of minimum area
        return Math.max((boundedFactor - 1) * Math.max(getArea(), 1), -getArea());
    }

    private double calcIntendedEnergyConsumption(double deltaArea) {
        DoubleParameter energyFactorParam = (deltaArea >= 0) ? parameters.growthCost : parameters.shrinkageYield;
        return deltaArea * energyFactorParam.getValue();
    }

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param availableEnergy the amount of energy available for growth; negative to shrink
     */
    @Deprecated
    public void requestResize_Old(double availableEnergy) {
        if (availableEnergy >= 0) {
            requestGrowth_Old(availableEnergy);
        } else {
            requestShrinkage_Old(-availableEnergy);
        }
    }

    private void requestGrowth_Old(double availableEnergy) {
        deltaArea = calcIntendedGrowth_Old(availableEnergy);
        intendedEnergyConsumption = deltaArea * parameters.growthCost.getValue();
    }

    private double calcIntendedGrowth_Old(double availableEnergy) {
        double requestedGrowth = availableEnergy / parameters.growthCost.getValue();
        double maxAllowedGrowth = Math.max(getArea(), 1) * parameters.maxGrowthRate_Old.getValue();
        return Math.min(requestedGrowth, maxAllowedGrowth);
    }

    private void requestShrinkage_Old(double requestedEnergy) {
        deltaArea = -calcIntendedShrinkage_Old(requestedEnergy);
        intendedEnergyConsumption = deltaArea * parameters.shrinkageYield.getValue();
    }

    private double calcIntendedShrinkage_Old(double requestedEnergy) {
        double requestedShrinkage = requestedEnergy / parameters.shrinkageYield.getValue();
        double maxAllowedShrinkage = getArea() * parameters.maxShrinkRate_Old.getValue();
        return Math.min(requestedShrinkage, maxAllowedShrinkage);
    }

    public double getIntendedEnergyConsumption() {
        return intendedEnergyConsumption;
    }

    public void scaleResizeRequest(double factor) {
        deltaArea *= factor;
        intendedEnergyConsumption *= factor;
    }

    public void resize() {
        addToArea(deltaArea);
        deltaArea = 0;
        intendedEnergyConsumption = 0;
    }

    public double getMaintenanceEnergy() {
        return getArea() * parameters.maintenanceCost.getValue();
    }

    public void decay() {
        multiplyAreaBy(1 - parameters.decayRate.getValue());
    }
}
