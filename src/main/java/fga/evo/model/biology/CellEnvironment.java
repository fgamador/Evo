package fga.evo.model.biology;

import fga.evo.model.physics.BallEnvironment;

public class CellEnvironment extends BallEnvironment {
    private double donatedEnergy;
    private double lightIntensity;
    private double shadowTransmissionFraction = 1;

    public double getDonatedEnergy() {
        return donatedEnergy;
    }

    public void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

    public void clearDonatedEnergy() {
        donatedEnergy = 0;
    }

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }

    public void addShadowing(double transmissionFraction) {
        this.shadowTransmissionFraction *= transmissionFraction;
    }

    public double getShadowTransmissionFraction() {
        return shadowTransmissionFraction;
    }

    public void resetShadowTransmissionFraction() {
        shadowTransmissionFraction = 1;
    }
}
