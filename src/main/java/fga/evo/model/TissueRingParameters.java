package fga.evo.model;

import fga.evo.model.physics.RingParameters;
import fga.evo.model.util.DoubleParameter;

public class TissueRingParameters extends RingParameters {
    DoubleParameter growthCost; // energy per area
    DoubleParameter maintenanceCost; // energy per area
    DoubleParameter shrinkageYield; // energy per area
    DoubleParameter maxGrowthRate; // fraction of current area
    DoubleParameter decayRate; // fraction of current area
}
