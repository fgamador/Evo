package fga.evo.model;

/**
 * Base class for the cell's rings of tissue.
 */
public abstract class TissueRing {
    protected double innerRadius;
    protected double outerRadius;
    protected double area;
    protected double mass;

    protected TissueRing(double outerRadius) {
        this.outerRadius = outerRadius;
    }

//    public final double getMaintenanceEnergy() {
//        return maintenanceCost * area;
//    }

    /**
     * Grows the ring by an amount determined by the specified energy.
     *
     * @param growthEnergy the amount of the cell's energy to use
     */
    protected final void growArea(final double growthEnergy, final double growthCost) {
        // TODO shrink if negative
        assert growthEnergy >= 0;
        area += growthEnergy / growthCost;
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
}
