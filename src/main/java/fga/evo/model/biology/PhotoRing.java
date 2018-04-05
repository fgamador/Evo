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
        parameters.maxShrinkRate = new DoubleParameter(1);
        parameters.decayRate = new DoubleParameter(0.1);
    }

    public PhotoRing(double area) {
        super(parameters, area);
    }

    @Override
    public void updateFromEnvironment(CellApi cell) {
        // photosynthesis
        final double lightIntensity = cell.getEnvironment().getLightIntensity();
        final double absorbedEnergy = lightIntensity * getOuterRadius() * calcPhotoAbsorptivity();
        cell.addEnergy(absorbedEnergy);
    }

    /**
     * Calculates the cell's efficiency at converting light into energy.
     * This depends on the thickness of the photosynthetic ring and starts
     * at 0 (no ring) and asymptotically approaches 1 (infinitely thick ring).
     *
     * @return the fraction of incident light that gets captured as energy
     */
    double calcPhotoAbsorptivity() {
        double thickness = getOuterRadius() - getInnerRadius();
        return 1 - (1 / (thickness + 1));
    }
}
