package fga.evo.model;

/**
 * A cell's air bubble, which allows it to regulate its buoyancy.
 */
public class FloatRing extends TissueRing {
    public static final Parameters parameters = new Parameters();
    static {
        parameters.setTissueDensity(0.0);
        parameters.setGrowthCost(0.01);
        parameters.setMaintenanceCost(0.005);
        //parameters.setShrinkageYield(0);
    }

    public FloatRing(double outerRadius, double innerArea) {
        super(parameters, outerRadius, innerArea);
    }
}
