package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Base class for the cell's rings of tissue.
 */
public class TissueRing extends Ring {
    private TissueRingParameters parameters;
    private double requestedDeltaArea;

    protected TissueRing(TissueRingParameters parameters, double outerRadius, double innerArea) {
        super(parameters, outerRadius, innerArea);
        this.parameters = parameters;
    }

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param growthEnergy the amount of energy available for growth; negative to shrink
     */
    public void requestResize(double growthEnergy) {
        if (growthEnergy >= 0) {
            double maxDeltaArea = Math.max(area, 1) * parameters.maxGrowthRate.getValue();
            requestedDeltaArea = Math.min(growthEnergy / parameters.growthCost.getValue(), maxDeltaArea);
        } else {
            requestedDeltaArea = Math.max(-area, growthEnergy / parameters.shrinkageYield.getValue());
        }
    }

    public double getRequestedEnergy() {
        return requestedDeltaArea *
                ((requestedDeltaArea >= 0) ? parameters.growthCost.getValue() : parameters.shrinkageYield.getValue());
    }

    public void scaleResizeRequest(double ratio) {
        requestedDeltaArea *= ratio;
    }

    public void resize() {
        area += requestedDeltaArea;
        requestedDeltaArea = 0;
    }

    public double getMaintenanceEnergy() {
        return area * parameters.maintenanceCost.getValue();
    }

    public void decay() {
        area *= 1 - parameters.decayRate.getValue();
    }
}
