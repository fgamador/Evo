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

    public void requestResize(double desiredDeltaArea) {
        deltaArea = boundDeltaArea(desiredDeltaArea);
        intendedEnergyConsumption = calcIntendedEnergyConsumption(deltaArea);
    }

    private double boundDeltaArea(double deltaArea) {
        double maxDeltaArea = parameters.maxGrowthRate.getValue() * Math.max(1, getArea());
        double minDeltaArea = -parameters.maxShrinkRate.getValue() * getArea();
        return Math.max(minDeltaArea, Math.min(maxDeltaArea, deltaArea));
    }

    private double calcIntendedEnergyConsumption(double deltaArea) {
        DoubleParameter energyFactorParam = (deltaArea >= 0) ? parameters.growthCost : parameters.shrinkageYield;
        return deltaArea * energyFactorParam.getValue();
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
