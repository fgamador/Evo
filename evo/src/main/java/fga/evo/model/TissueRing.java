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

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param growthEnergy the amount of energy available for growth; negative to shrink
     */
    public void requestResize(double growthEnergy) {
        if (growthEnergy >= 0) {
            double maxDeltaArea = Math.max(area, 1) * parameters.getMaxGrowthRate();
            requestedDeltaArea = Math.min(growthEnergy / parameters.getGrowthCost(), maxDeltaArea);
        } else {
            requestedDeltaArea = Math.max(-area, growthEnergy / parameters.getShrinkageYield());
        }
    }

    public double getRequestedEnergy() {
        return requestedDeltaArea *
                ((requestedDeltaArea >= 0) ? parameters.getGrowthCost() : parameters.getShrinkageYield());
    }

    public void scaleResizeRequest(double ratio) {
        requestedDeltaArea *= ratio;
    }

    public void resize() {
        area += requestedDeltaArea;
        requestedDeltaArea = 0;
    }

    public double getMaintenanceEnergy() {
        return area * parameters.maintenanceCost;
    }

    private void updateFromOuterRadius(double innerArea) {
        area = Math.PI * sqr(outerRadius) - innerArea;
        mass = parameters.tissueDensity * area;
    }

    public void updateFromArea(double innerRadius) {
        outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        mass = parameters.tissueDensity * area;
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

    public static class TissueRingParameters {
        private double tissueDensity; // mass per area
        private double growthCost; // energy per area
        private double maintenanceCost; // energy per area
        private double shrinkageYield; // energy per area
        private double maxGrowthRate; // fraction of current area

        public double getTissueDensity() {
            return tissueDensity;
        }

        public void setTissueDensity(double val) {
            tissueDensity = val;
        }

        public double getMaintenanceCost() {
            return maintenanceCost;
        }

        public void setMaintenanceCost(double val) {
            maintenanceCost = val;
        }

        public double getGrowthCost() {
            return growthCost;
        }

        public void setGrowthCost(double val) {
            growthCost = val;
        }

        public double getShrinkageYield() {
            return shrinkageYield;
        }

        public void setShrinkageYield(double val) {
            shrinkageYield = val;
        }

        public double getMaxGrowthRate() {
            return maxGrowthRate;
        }

        public void setMaxGrowthRate(double val) {
            this.maxGrowthRate = val;
        }
    }
}
