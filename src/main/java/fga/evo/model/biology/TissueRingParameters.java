package fga.evo.model.biology;

import fga.evo.model.physics.RingParameters;
import fga.evo.model.util.DoubleParameter;

public class TissueRingParameters extends RingParameters {
    public DoubleParameter growthCost; // energy per area
    public DoubleParameter maintenanceCost; // energy per area
    public DoubleParameter shrinkageYield; // energy per area
    public DoubleParameter maxGrowthRate; // fraction of current area
    public DoubleParameter decayRate; // fraction of current area
}
