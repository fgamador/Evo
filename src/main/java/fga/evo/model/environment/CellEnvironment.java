package fga.evo.model.environment;

import fga.evo.model.physics.NewtonianBodyEnvironment;

public class CellEnvironment extends NewtonianBodyEnvironment {
    private double donatedEnergy;
    private double lightIntensity;

    public double getDonatedEnergy() {
        return donatedEnergy;
    }

    public void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

    public double takeDonatedEnergy() {
        double energy = donatedEnergy;
        donatedEnergy = 0;
        return energy;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
