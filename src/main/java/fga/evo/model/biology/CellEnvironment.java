package fga.evo.model.biology;

import fga.evo.model.physics.BallEnvironment;
import fga.evo.model.physics.NewtonianBodyEnvironment;

public class CellEnvironment extends BallEnvironment {
    private double donatedEnergy;
    private double lightIntensity;
    private double transmissionFraction = 1;

    public double getDonatedEnergy() {
        return donatedEnergy;
    }

    public void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

    public double getAndClearDonatedEnergy() {
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

    public void addShadowing(double transmissionFraction) {
        this.transmissionFraction *= transmissionFraction;
    }

    public double getShadowTransmissionFraction() {
        return transmissionFraction;
    }
}
