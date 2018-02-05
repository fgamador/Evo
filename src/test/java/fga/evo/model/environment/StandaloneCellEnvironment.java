package fga.evo.model.environment;

public class StandaloneCellEnvironment extends CellEnvironment {
    private double centerX;
    private double centerY;

    public StandaloneCellEnvironment(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterY() {
        return centerY;
    }
}
