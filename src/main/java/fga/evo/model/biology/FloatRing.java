package fga.evo.model.biology;

import fga.evo.model.util.DoubleParameter;

/**
 * A cell's air bubble, which allows it to regulate its buoyancy.
 */
public class FloatRing extends TissueRing {
    public static final TissueRingParameters parameters = new TissueRingParameters();

    static {
        parameters.density = new DoubleParameter(0.0001);
        parameters.growthCost = new DoubleParameter(0.01);
        parameters.maintenanceCost = new DoubleParameter(0.005);
        parameters.shrinkageYield = new DoubleParameter(0);
        parameters.maxResizeFactor_New = new DoubleParameter(10);
        parameters.minResizeFactor_New = new DoubleParameter(0.1);
        parameters.maxGrowthRate = new DoubleParameter(1000);
        parameters.maxShrinkRate = new DoubleParameter(1);
        parameters.decayRate = new DoubleParameter(0.1);
    }

    public FloatRing(double area) {
        super(parameters, area);
    }
}
