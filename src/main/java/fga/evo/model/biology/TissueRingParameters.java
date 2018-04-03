package fga.evo.model.biology;

import fga.evo.model.geometry.RingParameters;
import fga.evo.model.util.DoubleParameter;

public class TissueRingParameters extends RingParameters {
    public DoubleParameter growthCost; // energy per area
    public DoubleParameter maintenanceCost; // energy per area
    public DoubleParameter shrinkageYield; // energy per area
    public DoubleParameter maxResizeFactor_New; // growth-rate bound, >= 1
    public DoubleParameter minResizeFactor_New; // shrink-rate bound, <= 1
    public DoubleParameter maxGrowthRate; // fraction of current area, 0-*
    public DoubleParameter maxShrinkRate; // fraction of current area, 0-1
    public DoubleParameter decayRate; // fraction of current area
}
