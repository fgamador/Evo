package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Base class for the cell's rings of tissue.
 */
public abstract class TissueRing {
    private TissueRingParameters parameters;
    protected double innerRadius;
    protected double outerRadius;
    protected double area;
    protected double mass;
    private double requestedDeltaArea;

    protected TissueRing(TissueRingParameters parameters, double outerRadius, double innerArea) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
        updateFromOuterRadius(innerArea);
    }

    public void initArea(double area) {
        checkAreaAndOuterRadiusAreUnset();
        this.area = area;
    }

    public void initOuterRadius(double radius) {
        checkAreaAndOuterRadiusAreUnset();
        this.outerRadius = radius;
    }

    private void checkAreaAndOuterRadiusAreUnset() {
        if (this.area != 0) {
            throw new IllegalStateException("Area is already set to " + this.area);
        }
        if (this.outerRadius != 0) {
            throw new IllegalStateException("Outer radius is already set to " + this.outerRadius);
        }
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

    public void updateFromAreaOrOuterRadius(TissueRing innerRing) {
        double innerRadius = (innerRing != null) ? innerRing.outerRadius : 0;
        if (area != 0) {
            updateFromArea(innerRadius);
        } else {
            outerRadius = Math.max(outerRadius, innerRadius);
            double innerArea = (innerRing != null) ? innerRing.area : 0;
            updateFromOuterRadius(innerArea);
        }
    }

    private void updateFromOuterRadius(double innerArea) {
        area = Math.PI * sqr(outerRadius) - innerArea;
        mass = parameters.tissueDensity.getValue() * area;
    }

    public void updateFromArea(double innerRadius) {
        outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        mass = parameters.tissueDensity.getValue() * area;
    }

    public void decay() {
        area *= 1 - parameters.decayRate.getValue();
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public double getArea() {
        return area;
    }

    public double getMass() {
        return mass;
    }
}
