package fga.evo.model.util;

public class DecayingAccumulator {
    private DoubleParameter retentionRate;
    private double total;

    public DecayingAccumulator(DoubleParameter retentionRate) {
        this.retentionRate = retentionRate;
    }

    public double getTotal() {
        return total;
    }

    public void addValue(double value) {
        total += value;
    }

    public void decay() {
        total *= retentionRate.getValue();
    }
}
