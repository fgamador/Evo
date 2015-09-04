package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Base class for the cell's rings of tissue.
 */
public abstract class TissueRing {
    private Parameters parameters;
    protected double innerRadius;
    protected double outerRadius;
    protected double area;
    protected double mass;
    private double requestedDeltaArea;

    protected TissueRing(Parameters parameters, double outerRadius, double innerArea) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
        updateFromOuterRadius(innerArea);
    }

    // New thinkings:
    //    double getGrowthCost(double growthFraction)
    //    void grow(double growthFraction)
    //    double shrink(double shrinkageFraction)
    // The neural net decides to grow some rings and shrink some rings by some fraction.
    // The control handles this as follows:
    // 1) Do all the shrinkage and add the returned energy to the pot.
    // 2) Ask about the total cost of all the growth.
    // 3) Do all the growth, scaled as necessary per the available energy.

    /**
     * Records a request that the ring's area grow or shrink using a specified amount of energy.
     *
     * @param growthEnergy the amount of energy available for growth; negative to shrink
     */
    public void requestResize(double growthEnergy) {
        if (growthEnergy >= 0) {
            requestedDeltaArea = growthEnergy / parameters.getGrowthCost();
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

    public final double getMaintenanceEnergy() {
        return area * parameters.maintenanceCost;
    }

    private void updateFromOuterRadius(double innerArea) {
        area = Math.PI * sqr(outerRadius) - innerArea;
        mass = parameters.tissueDensity * area;
    }

    public final void updateFromArea(double innerRadius) {
        outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        mass = parameters.tissueDensity * area;
    }

    public final double getOuterRadius() {
        return outerRadius;
    }

    public final double getArea() {
        return area;
    }

    public final double getMass() {
        return mass;
    }

    public static class Parameters {
        private double tissueDensity; // mass per area
        private double growthCost; // energy per area
        private double maintenanceCost; // energy per area
        private double shrinkageYield; // energy per area

        public final double getTissueDensity() {
            return tissueDensity;
        }

        public final void setTissueDensity(double val) {
            tissueDensity = val;
        }

        public final double getMaintenanceCost() {
            return maintenanceCost;
        }

        public final void setMaintenanceCost(double val) {
            maintenanceCost = val;
        }

        public final double getGrowthCost() {
            return growthCost;
        }

        public final void setGrowthCost(double val) {
            growthCost = val;
        }

        public final double getShrinkageYield() {
            return shrinkageYield;
        }

        public final void setShrinkageYield(double val) {
            shrinkageYield = val;
        }
    }
}
