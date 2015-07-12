package fga.evo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The world in which the cells live. The root container of the whole model. The entry point for simulation clock ticks.
 */
public class World {
    private Box box;
    private List<Cell> cells = new ArrayList<>();
    private Dragger dragger;

    public World(double width, double height) {
        box = new Box(width, height);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    /**
     * Propagates a simulation clock tick through the model.
     */
    public void tick() {
        if (dragger != null) {
            dragger.addForceToCell();
        }

        for (int i = 0; i < cells.size(); i++) {
            addForcesToCell(cells.get(i), i);
        }

        for (Cell cell : cells) {
            cell.move();
        }
    }

    private void addForcesToCell(Cell cell, int index) {
        box.addWallCollisionForcesToCell(cell);

        // TODO Idea: keep cells sorted by centerX, with each cell knowing its index.
        // Each cell need check only those others with greater indexes until it
        // finds another cell whose centerX is beyond the max radius plus the cell
        // radius. Works for finding shadowing, too.
        for (int j = index + 1; j < cells.size(); j++) {
            Cell cell2 = cells.get(j);
            cell.addInterCellForces(cell2);
        }
    }

    public Collection<Cell> getCells() {
        return cells;
    }

    public void startDrag(Cell cell) {
        dragger = new Dragger(cell);
    }

    public void setDragPoint(double x, double y) {
        dragger.setPosition(x, y);
    }

    public void endDrag() {
        dragger = null;
    }

    public boolean isDragging() {
        return dragger != null;
    }
}
