package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public class CellEnvironment extends NewtonianBodyEnvironment {
    public double donatedEnergy;
    private double lightIntensity;

    public double getDonatedEnergy() {
        return donatedEnergy;
    }

    public void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
