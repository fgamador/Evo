package fga.evo.model;

/**
 * A cell's ring of photosynthetic tissue.
 */
public class PhotoRing extends TissueRing {
    private static double tissueDensity = 0.011; // mass per area
    private static double growthCost = 1.1; // energy per area
    private static double maintenanceCost = 0.005; // energy per area
    //private static double shrinkageYield = 0.1; // energy per area

    public PhotoRing(double outerRadius) {
        super(outerRadius);
    }

    /**
     * Converts incoming light into energy.
     *
     * @param lightIntensity incoming light intensity, as energy per width
     */
    public final double photosynthesize(double lightIntensity) {
        return lightIntensity * outerRadius * calcPhotoAbsorptivity();
    }

    /**
     * Calculates the cell's efficiency at converting light into energy.
     * This depends on the thickness of the photosynthetic ring and starts
     * at 0 (no ring) and asymptotically approaches 1 (infinite thickness).
     *
     * @return the fraction of incident light that gets captured as energy
     */
    public final double calcPhotoAbsorptivity() {
        final double thickness = outerRadius - innerRadius;
        return 1 - (1 / (thickness + 1));
    }

    public final double getMaintenanceEnergy() {
        return maintenanceCost * area;
    }

    /**
     * Grows the photosynthetic ring by an amount determined by the specified
     * energy.
     *
     * @param growthEnergy the amount of the cell's energy to use
     */
    public final void growArea(double growthEnergy) {
        super.growArea(growthEnergy, growthCost);
    }

    public static double sqr(double val) {
        return val * val;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getTissueDensity() {
        return tissueDensity;
    }

    public static void setTissueDensity(double val) {
        tissueDensity = val;
    }

    public static double getMaintenanceCost() {
        return maintenanceCost;
    }

    public static void setMaintenanceCost(double val) {
        maintenanceCost = val;
    }

    public static double getGrowthCost() {
        return growthCost;
    }

    public static void setGrowthCost(double val) {
        growthCost = val;
    }
}
