package fga.evo.model;

/**
 * A cell's ring of photosynthetic tissue.
 */
public class PhotoRing extends TissueRing {
    public static final TissueRingParameters parameters = new TissueRingParameters();

    static {
        parameters.setTissueDensity(0.011);
        parameters.setGrowthCost(1.1);
        parameters.setMaintenanceCost(0.005);
        parameters.setShrinkageYield(0.1);
        parameters.setMaxGrowthRate(1000);
    }

    public PhotoRing(double outerRadius, double innerArea) {
        super(parameters, outerRadius, innerArea);
    }

    /**
     * Converts incoming light into energy.
     *
     * @param lightIntensity incoming light intensity, as energy per width
     */
    public double photosynthesize(double lightIntensity) {
        return lightIntensity * outerRadius * calcPhotoAbsorptivity();
    }

    /**
     * Calculates the cell's efficiency at converting light into energy.
     * This depends on the thickness of the photosynthetic ring and starts
     * at 0 (no ring) and asymptotically approaches 1 (infinitely thick ring).
     *
     * @return the fraction of incident light that gets captured as energy
     */
    public double calcPhotoAbsorptivity() {
        double thickness = outerRadius - innerRadius;
        return 1 - (1 / (thickness + 1));
    }
}
