package fga.evo.model.biology;

import fga.evo.model.environment.CellEnvironment;

public class TissueRingCellApiWithEnvironment implements TissueRing.CellApi {
    private CellEnvironment environment = new CellEnvironment();
    public double energy;

    @Override
    public CellEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void addEnergy(double energy) {
        this.energy += energy;
    }
}
