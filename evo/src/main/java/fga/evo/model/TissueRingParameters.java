package fga.evo.model;

public class TissueRingParameters {
    DoubleParameter tissueDensity; // mass per area
    DoubleParameter growthCost; // energy per area
    DoubleParameter maintenanceCost; // energy per area
    DoubleParameter shrinkageYield; // energy per area
    DoubleParameter maxGrowthRate; // fraction of current area

    // TODO lose getters and setters
    public double getTissueDensity() {
        return tissueDensity.getValue();
    }

    public void setTissueDensity(double val) {
        tissueDensity.setValue(val);
    }

    public double getMaintenanceCost() {
        return maintenanceCost.getValue();
    }

    public double getGrowthCost() {
        return growthCost.getValue();
    }

    public double getShrinkageYield() {
        return shrinkageYield.getValue();
    }

    public double getMaxGrowthRate() {
        return maxGrowthRate.getValue();
    }

    public void setMaxGrowthRate(double val) {
        this.maxGrowthRate.setValue(val);
    }
}
