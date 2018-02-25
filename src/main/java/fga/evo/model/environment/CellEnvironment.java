package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public class CellEnvironment extends NewtonianBodyEnvironment {
    private double lightIntensity;

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
