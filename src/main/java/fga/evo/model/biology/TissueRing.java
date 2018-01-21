package fga.evo.model.biology;

import fga.evo.model.physics.Ring;
import fga.evo.model.util.DoubleParameter;

/**
 * Base class for the cell's rings of tissue.
 */
public class TissueRing extends Ring {
    private TissueRingParameters parameters;
    private double intendedEnergyConsumption;

    protected TissueRing(TissueRingParameters parameters, double outerRadius) {
        super(parameters, outerRadius);
        this.parameters = parameters;
    }

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param availableEnergy the amount of energy available for growth; negative to shrink
     */
    public void requestResize(double availableEnergy) {
        if (availableEnergy >= 0) {
            requestGrowth(availableEnergy);
        } else {
            requestShrinkage(-availableEnergy);
        }
    }

    private void requestGrowth(double availableEnergy) {
        intendedEnergyConsumption = calcIntendedGrowth(availableEnergy) * parameters.growthCost.getValue();
    }

    private double calcIntendedGrowth(double availableEnergy) {
        double requestedGrowth = availableEnergy / parameters.growthCost.getValue();
        double maxAllowedGrowth = Math.max(getArea(), 1) * parameters.maxGrowthRate.getValue();
        return Math.min(requestedGrowth, maxAllowedGrowth);
    }

    private void requestShrinkage(double requestedEnergy) {
        intendedEnergyConsumption = -calcIntendedShrinkage(requestedEnergy) * parameters.shrinkageYield.getValue();
    }

    private double calcIntendedShrinkage(double requestedEnergy) {
        double requestedShrinkage = requestedEnergy / parameters.shrinkageYield.getValue();
        double maxAllowedShrinkage = getArea() * parameters.maxShrinkRate.getValue();
        return Math.min(requestedShrinkage, maxAllowedShrinkage);
    }

    public double getIntendedEnergyConsumption() {
        return intendedEnergyConsumption;
    }

    public void scaleResizeRequest(double ratio) {
        intendedEnergyConsumption *= ratio;
    }

    public void resize() {
        DoubleParameter energyPerArea = (intendedEnergyConsumption >= 0) ? parameters.growthCost : parameters.shrinkageYield;
        resize(intendedEnergyConsumption / energyPerArea.getValue());
        intendedEnergyConsumption = 0;
    }

    public double getMaintenanceEnergy() {
        return getArea() * parameters.maintenanceCost.getValue();
    }

    public void decay() {
        setArea(getArea() * (1 - parameters.decayRate.getValue()));
    }
}
