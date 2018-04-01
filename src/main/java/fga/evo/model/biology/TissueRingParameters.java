package fga.evo.model.biology;

import fga.evo.model.geometry.RingParameters;
import fga.evo.model.util.DoubleParameter;

public class TissueRingParameters extends RingParameters {
    public DoubleParameter growthCost; // energy per area
    public DoubleParameter maintenanceCost; // energy per area
    public DoubleParameter shrinkageYield; // energy per area
    public DoubleParameter maxResizeFactor; // growth-rate bound, >= 1
    public DoubleParameter minResizeFactor; // shrink-rate bound, <= 1
    public DoubleParameter maxGrowthRate_Old; // fraction of current area
    public DoubleParameter maxShrinkRate_Old; // fraction of current area, <= 1
    public DoubleParameter decayRate; // fraction of current area
}
