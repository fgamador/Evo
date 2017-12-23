package fga.evo.model;

import fga.evo.model.physics.PairCollision;

import java.util.List;

class OverlapDetection {
    public void addCollisionForces(List<Cell> cells) {
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);

            // TODO Idea: keep cells sorted by centerX. Need check a cell against
            // only those others with greater indexes until we find another cell
            // whose centerX is beyond the max radius plus the first cell's
            // radius. Works for finding shadowing, too.
            for (int j = i + 1; j < cells.size(); j++) {
                Cell cell2 = cells.get(j);
                if (!cell.isBondedTo(cell2))
                    PairCollision.addForces(cell, cell2);
            }
        }
    }
}
