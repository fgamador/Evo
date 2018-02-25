package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public class CellEnvironment extends NewtonianBodyEnvironment {
    private double lightIntensity;

    public double getRadius() {
        return -1;
    }

    public double getCenterX() {
        return -1;
    }

    public double getCenterY() {
        return -1;
    }

    public double getVelocityX() {
        return -1;
    }

    public double getVelocityY() {
        return -1;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
