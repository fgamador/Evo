package fga.evo.model.biology;

import fga.evo.model.util.DoubleParameter;

/**
 * A cell's ring of photosynthetic tissue.
 */
public class PhotoRing extends TissueRing {
    public static final TissueRingParameters parameters = new TissueRingParameters();

    static {
        parameters.density = new DoubleParameter(0.1);
        parameters.growthCost = new DoubleParameter(1.1);
        parameters.maintenanceCost = new DoubleParameter(0.005);
        parameters.shrinkageYield = new DoubleParameter(0.1);
        parameters.maxGrowthRate = new DoubleParameter(1000);
        parameters.decayRate = new DoubleParameter(0.1);
    }

    public PhotoRing(double outerRadius) {
        super(parameters, outerRadius);
    }

    /**
     * Converts incoming light into energy.
     *
     * @param lightIntensity incoming light intensity, as energy per width
     */
    public double photosynthesize(double lightIntensity) {
        return lightIntensity * getOuterRadius() * calcPhotoAbsorptivity();
    }

    /**
     * Calculates the cell's efficiency at converting light into energy.
     * This depends on the thickness of the photosynthetic ring and starts
     * at 0 (no ring) and asymptotically approaches 1 (infinitely thick ring).
     *
     * @return the fraction of incident light that gets captured as energy
     */
    public double calcPhotoAbsorptivity() {
        double thickness = getOuterRadius() - getInnerRadius();
        return 1 - (1 / (thickness + 1));
    }
}
