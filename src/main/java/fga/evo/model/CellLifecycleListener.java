package fga.evo.model;

import fga.evo.model.physics.PairBond;

public interface CellLifecycleListener {
    void onCellBorn(Cell cell);

    void onCellDied(Cell cell);

    void onBondFormed(PairBond bond);

    void onBondBroken(PairBond bond);

    public static final CellLifecycleListener NULL_LISTENER = new CellLifecycleListener() {
        @Override
        public void onCellBorn(Cell cell) {
        }

        @Override
        public void onCellDied(Cell cell) {
        }

        @Override
        public void onBondFormed(PairBond bond) {
        }

        @Override
        public void onBondBroken(PairBond bond) {
        }
    };
}
