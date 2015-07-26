package fga.evo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The world in which the cells live. The root container of the whole model. The entry point for simulation clock ticks.
 */
public class World {
    private Box box;
    private Fluid fluid;
    private Puller puller;
    private List<Cell> cells = new ArrayList<>();

    public void setBox(final Box val) {
        box = val;
    }

    public void setFluid(final Fluid val) {
        fluid = val;
    }

    public void addCell(final Cell cell) {
        cells.add(cell);
    }

    /**
     * Propagates a simulation clock tick through the model.
     */
    public void tick() {
        if (puller != null) {
            puller.addForceToCell();
        }

        // TODO should be easily parallelizeable
        for (int i = 0; i < cells.size(); i++) {
            addForcesToCell(i);
        }

        // TODO should be easily parallelizeable
        for (Cell cell : cells) {
            cell.move();
        }
    }

    private void addForcesToCell(final int index) {
        Cell cell = cells.get(index);

        if (box != null) {
            box.addWallCollisionForcesToCell(cell);
        }

        if (fluid != null) {
            fluid.addDragForceToCell(cell);
        }

        // TODO Idea: keep cells sorted by centerX. Need check a cell against
        // only those others with greater indexes until we find another cell
        // whose centerX is beyond the max radius plus the first cell's
        // radius. Works for finding shadowing, too.
        for (int j = index + 1; j < cells.size(); j++) {
            Cell cell2 = cells.get(j);
            cell.addInterCellForces(cell2);
        }
    }

    public Collection<Cell> getCells() {
        return cells;
    }

    public void startPull(final Cell cell) {
        puller = new Puller(cell);
    }

    public void setPullPoint(final double x, final double y) {
        puller.setPosition(x, y);
    }

    public void endPull() {
        puller = null;
    }

    public boolean isPulling() {
        return puller != null;
    }
}
