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
    private double requestedArea;

    protected TissueRing(Parameters parameters, double outerRadius, double innerArea) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
        updateFromOuterRadius(innerArea);
        requestedArea = area;
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
     * Records a request that the ring's area change to a specified value.
     *
     * @param area the desired new area
     */
    public void requestResize(double area) {
        requestedArea = Math.max(area, 0);
    }

    public double getRequestedEnergy() {
        double requestedDeltaArea = requestedArea - area;
        return requestedDeltaArea *
                ((requestedDeltaArea >= 0) ? parameters.getGrowthCost() : parameters.getShrinkageYield());
    }

    public void scaleResizeRequest(double ratio) {
        requestedArea = area + ratio * (requestedArea - area);
    }

    public void resize() {
        area = requestedArea;
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
