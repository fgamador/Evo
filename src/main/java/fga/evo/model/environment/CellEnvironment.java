package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyForces;

public abstract class CellEnvironment extends NewtonianBodyForces {
    private double lightIntensity;

    public abstract double getCenterX();

    public abstract double getCenterY();

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
