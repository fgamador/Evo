package fga.evo.model;

public class TissueRingParameters {
    // TODO lose initial values; make ring instantiate the params with default values
    DoubleParameter tissueDensity = new DoubleParameter(0); // mass per area
    DoubleParameter growthCost = new DoubleParameter(0); // energy per area
    DoubleParameter maintenanceCost = new DoubleParameter(0); // energy per area
    DoubleParameter shrinkageYield = new DoubleParameter(0); // energy per area
    DoubleParameter maxGrowthRate = new DoubleParameter(0); // fraction of current area

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

    public void setMaintenanceCost(double val) {
        maintenanceCost.setValue(val);
    }

    public double getGrowthCost() {
        return growthCost.getValue();
    }

    public void setGrowthCost(double val) {
        growthCost.setValue(val);
    }

    public double getShrinkageYield() {
        return shrinkageYield.getValue();
    }

    public void setShrinkageYield(double val) {
        shrinkageYield.setValue(val);
    }

    public double getMaxGrowthRate() {
        return maxGrowthRate.getValue();
    }

    public void setMaxGrowthRate(double val) {
        this.maxGrowthRate.setValue(val);
    }

    // TODO lose this
    public TissueRingParameters register(String prefix) {
        tissueDensity.register(prefix + ".tissueDensity");
        growthCost.register(prefix + ".growthCost");
        maintenanceCost.register(prefix + ".maintenanceCost");
        shrinkageYield.register(prefix + ".shrinkageYield");
        maxGrowthRate.register(prefix + ".maxGrowthRate");
        return this;
    }
}
