package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public abstract class CellEnvironment extends NewtonianBodyEnvironment {
    private double lightIntensity;

    public abstract double getRadius();

    public abstract double getVelocityX();

    public abstract double getVelocityY();

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
