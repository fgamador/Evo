package fga.evo.model;

public class TouchSensor {
    private static double retentionRate = 0.9;

    private double recentOverlap;

    public double getRecentOverlap() {
        return recentOverlap;
    }

    public void addOverlap(double overlap) {
        recentOverlap += overlap;
    }

    public void endTick() {
        recentOverlap *= retentionRate;
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
