package fga.evo.model.biology;

import fga.evo.model.physics.Ring;

/**
 * Base class for the cell's rings of tissue.
 */
public class TissueRing extends Ring {
    private TissueRingParameters parameters;
    private double deltaArea;
    private double consumedEnergy;

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
        deltaArea = calcGrowth(availableEnergy);
        consumedEnergy = deltaArea * parameters.growthCost.getValue();
    }

    private double calcGrowth(double availableEnergy) {
        double requestedGrowth = availableEnergy / parameters.growthCost.getValue();
        double maxAllowedGrowth = Math.max(getArea(), 1) * parameters.maxGrowthRate.getValue();
        return Math.min(requestedGrowth, maxAllowedGrowth);
    }

    private void requestShrinkage(double requestedEnergy) {
        deltaArea = -calcShrinkage(requestedEnergy);
        consumedEnergy = deltaArea * parameters.shrinkageYield.getValue();
    }

    private double calcShrinkage(double requestedEnergy) {
        double requestedShrinkage = requestedEnergy / parameters.shrinkageYield.getValue();
        double maxAllowedShrinkage = getArea() * parameters.maxShrinkRate.getValue();
        return Math.min(requestedShrinkage, maxAllowedShrinkage);
    }

    public double getConsumedEnergy() {
        return consumedEnergy;
    }

    public void scaleResizeRequest(double ratio) {
        deltaArea *= ratio;
        consumedEnergy *= ratio;
    }

    public void resize() {
        deltaArea = (consumedEnergy >= 0)
                ? consumedEnergy / parameters.growthCost.getValue()
                : consumedEnergy / parameters.shrinkageYield.getValue();
        resize(deltaArea);
        deltaArea = 0;
        consumedEnergy = 0;
    }

    public double getMaintenanceEnergy() {
        return getArea() * parameters.maintenanceCost.getValue();
    }

    public void decay() {
        setArea(getArea() * (1 - parameters.decayRate.getValue()));
    }
}
