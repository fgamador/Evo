package fga.evo.model;

public class DecayingAccumulator {
    // TODO pass this in from client
    static DoubleParameter retentionRate = new DoubleParameter(0.9).register("retentionRate");

    private double total;

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
