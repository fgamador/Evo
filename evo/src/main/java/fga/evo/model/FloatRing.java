package fga.evo.model;

/**
 * A cell's air bubble, which allows it to regulate its buoyancy.
 */
public class FloatRing extends TissueRing {
    public static final TissueRingParameters parameters = new TissueRingParameters().register("FloatRing");

    static {
        parameters.setTissueDensity(0.0001);
        parameters.setGrowthCost(0.01);
        parameters.setMaintenanceCost(0.005);
        parameters.setShrinkageYield(0);
        parameters.setMaxGrowthRate(1000);
    }

    public FloatRing(double outerRadius, double innerArea) {
        super(parameters, outerRadius, innerArea);
    }
}
