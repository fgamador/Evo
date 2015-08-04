package fga.evo.model;

/**
 * Base class for the cell's rings of tissue.
 */
public abstract class TissueRing {
    private Parameters parameters;
    protected double innerRadius;
    protected double outerRadius;
    protected double area;
    protected double mass;

    protected TissueRing(Parameters parameters, double outerRadius) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
    }

    /**
     * Grows the ring by an amount determined by the specified energy.
     *
     * @param growthEnergy the amount of the cell's energy to use
     */
    public final void growArea(final double growthEnergy) {
        // TODO shrink if negative
        assert growthEnergy >= 0;
        area += growthEnergy / parameters.growthCost;
    }

    public final double getMaintenanceEnergy() {
        return area * parameters.maintenanceCost;
    }

    public final void outerRadiusToArea() { // TODO innerArea
//        area = Math.PI * sqr(outerRadius) - innerArea;
        area = Math.PI * sqr(outerRadius);
    }

    public final void areaToOuterRadius() { // TODO innerRadius
//        outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        outerRadius = Math.sqrt(area / Math.PI);
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

    protected static double sqr(double val) {
        return val * val;
    }

    public static class Parameters {
        private double tissueDensity; // mass per area
        private double growthCost; // energy per area
        private double maintenanceCost; // energy per area
        //private  double shrinkageYield; // energy per area

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
    }
}
