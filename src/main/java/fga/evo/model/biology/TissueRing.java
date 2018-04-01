package fga.evo.model.biology;

import fga.evo.model.geometry.Ring;

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

    public void requestResize(double factor) {
        deltaArea = factor * getArea() - getArea();
    }

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param availableEnergy the amount of energy available for growth; negative to shrink
     */
    public void requestResize_Old(double availableEnergy) {
        if (availableEnergy >= 0) {
            requestGrowth(availableEnergy);
        } else {
            requestShrinkage(-availableEnergy);
        }
    }

    private void requestGrowth(double availableEnergy) {
        deltaArea = calcIntendedGrowth(availableEnergy);
        intendedEnergyConsumption = deltaArea * parameters.growthCost.getValue();
    }

    private double calcIntendedGrowth(double availableEnergy) {
        double requestedGrowth = availableEnergy / parameters.growthCost.getValue();
        double maxAllowedGrowth = Math.max(getArea(), 1) * parameters.maxGrowthRate.getValue();
        return Math.min(requestedGrowth, maxAllowedGrowth);
    }

    private void requestShrinkage(double requestedEnergy) {
        deltaArea = -calcIntendedShrinkage(requestedEnergy);
        intendedEnergyConsumption = deltaArea * parameters.shrinkageYield.getValue();
    }

    private double calcIntendedShrinkage(double requestedEnergy) {
        double requestedShrinkage = requestedEnergy / parameters.shrinkageYield.getValue();
        double maxAllowedShrinkage = getArea() * parameters.maxShrinkRate.getValue();
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
