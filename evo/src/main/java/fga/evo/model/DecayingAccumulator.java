package fga.evo.model;

public class DecayingAccumulator {
    private static double retentionRate = 0.9;

    private double total;

    public double getTotal() {
        return total;
    }

    public void addValue(double value) {
        total += value;
    }

    public void decay() {
        total *= retentionRate;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getRetentionRate() {
        return retentionRate;
    }

    public static void setRetentionRate(double val) {
        retentionRate = val;
    }
}
