package fga.evo.model;

import fga.evo.model.physics.PairBond;

import java.util.Collections;
import java.util.List;

public class Spawnings {
    public static final Spawnings EMPTY = new Spawnings(Collections.emptyList(), Collections.emptyList());

    private List<Cell> cells;
    private List<PairBond> bonds;

    public Spawnings(List<Cell> cells, List<PairBond> bonds) {
        this.cells = cells;
        this.bonds = bonds;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public List<PairBond> getBonds() {
        return bonds;
    }
}
