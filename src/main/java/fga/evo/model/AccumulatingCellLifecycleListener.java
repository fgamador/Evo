package fga.evo.model;

import fga.evo.model.physics.PairBond;

import java.util.ArrayList;
import java.util.List;

public class AccumulatingCellLifecycleListener implements CellLifecycleListener {
    public List<Cell> bornCells = new ArrayList<>();
    public List<Cell> deadCells = new ArrayList<>();
    public List<PairBond> formedBonds = new ArrayList<>();
    public List<PairBond> brokenBonds = new ArrayList<>();

    public void clear() {
        bornCells.clear();
        deadCells.clear();
        formedBonds.clear();
        brokenBonds.clear();
    }

    @Override
    public void onCellBorn(Cell cell) {
        bornCells.add(cell);
    }

    @Override
    public void onCellDied(Cell cell) {
        deadCells.add(cell);
    }

    @Override
    public void onBondFormed(PairBond bond) {
        formedBonds.add(bond);
    }

    @Override
    public void onBondBroken(PairBond bond) {
        brokenBonds.add(bond);
    }
}
