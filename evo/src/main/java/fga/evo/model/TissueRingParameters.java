package fga.evo.model;

public class TissueRingParameters {
    DoubleParameter tissueDensity; // mass per area
    DoubleParameter growthCost; // energy per area
    DoubleParameter maintenanceCost; // energy per area
    DoubleParameter shrinkageYield; // energy per area
    DoubleParameter maxGrowthRate; // fraction of current area
    DoubleParameter decayRate; // fraction of current area
}
