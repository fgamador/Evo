package fga.evo.model.physics;

public class BallEnvironment {
    private double totalOverlap;

    public void addOverlap(double overlap) {
        totalOverlap += overlap;
    }

    public double getTotalOverlap() {
        return totalOverlap;
    }

    public void getAndClearTotalOverlap() {
        double overlap = totalOverlap;
        totalOverlap = 0;
    }
}
