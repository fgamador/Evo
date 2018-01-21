package fga.evo.model.biology;

import fga.evo.model.physics.Ring;

/**
 * Base class for the cell's rings of tissue.
 */
public class TissueRing extends Ring {
    private TissueRingParameters parameters;
    private double requestedDeltaArea;

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
        requestedDeltaArea = calcActualGrowth(availableEnergy);
    }

    private double calcActualGrowth(double availableEnergy) {
        double requestedGrowth = availableEnergy / parameters.growthCost.getValue();
        double maxAllowedGrowth = Math.max(getArea(), 1) * parameters.maxGrowthRate.getValue();
        return Math.min(requestedGrowth, maxAllowedGrowth);
    }

    private void requestShrinkage(double requestedEnergy) {
        requestedDeltaArea = -calcActualShrinkage(requestedEnergy);
    }

    private double calcActualShrinkage(double requestedEnergy) {
        double requestedShrinkage = requestedEnergy / parameters.shrinkageYield.getValue();
        double maxAllowedShrinkage = getArea() * parameters.maxShrinkRate.getValue();
        return Math.min(requestedShrinkage, maxAllowedShrinkage);
    }

    public double getRequestedEnergy() {
        return requestedDeltaArea *
                ((requestedDeltaArea >= 0) ? parameters.growthCost.getValue() : parameters.shrinkageYield.getValue());
    }

    public void scaleResizeRequest(double ratio) {
        requestedDeltaArea *= ratio;
    }

    public void resize() {
        resize(requestedDeltaArea);
        requestedDeltaArea = 0;
    }

    public double getMaintenanceEnergy() {
        return getArea() * parameters.maintenanceCost.getValue();
    }

    public void decay() {
        setArea(getArea() * (1 - parameters.decayRate.getValue()));
    }
}
