package fga.evo.model.environment;

public class StandaloneCellEnvironment extends CellEnvironment {
    private double radius;
    private double centerX;
    private double centerY;
    private double velocityX;
    private double velocityY;

    public StandaloneCellEnvironment(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterY() {
        return centerY;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }
}
