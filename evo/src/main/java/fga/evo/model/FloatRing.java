package fga.evo.model;

/**
 * A cell's air bubble, which allows it to regulate its buoyancy.
 */
public class FloatRing extends TissueRing {
    public static final TissueRingParameters parameters = new TissueRingParameters();

    static {
        parameters.tissueDensity = new DoubleParameter(0.0001);
        parameters.growthCost = new DoubleParameter(0.01);
        parameters.maintenanceCost = new DoubleParameter(0.005);
        parameters.shrinkageYield = new DoubleParameter(0);
        parameters.maxGrowthRate = new DoubleParameter(1000);
        parameters.decayRate = new DoubleParameter(0.1);
    }

    public FloatRing(double outerRadius, double innerArea) {
        super(parameters, outerRadius, innerArea);
    }
}
