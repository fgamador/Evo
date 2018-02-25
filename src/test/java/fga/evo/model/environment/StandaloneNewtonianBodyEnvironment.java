package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public class StandaloneNewtonianBodyEnvironment extends NewtonianBodyEnvironment {
    private double centerX;
    private double centerY;

    public StandaloneNewtonianBodyEnvironment(double centerX, double centerY) {
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
