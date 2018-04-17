package fga.evo.model.physics;

public class BallEnvironment {
    private double totalOverlap;

    public void addOverlap(double overlap) {
        totalOverlap += overlap;
    }

    public double getAndClearTotalOverlap() {
        double overlap = totalOverlap;
        totalOverlap = 0;
        return overlap;
    }
}
