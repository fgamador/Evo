package fga.evo.model.environment;

import fga.evo.model.biology.Cell;

/**
 * Something in the cells' environment that can affect them.
 */
public abstract class EnvironmentalInfluence {
    public abstract void updateEnvironment(CellEnvironment environment, Cell cell);

    // TODO make this obsolete
    public void addEnergyToCell(Cell cell) {
        updateEnvironment(cell.getEnvironment(), cell);
    }

    // TODO make this obsolete
    public void addForcesToCell(Cell cell) {
        //updateEnvironment(cell.getEnvironment(), cell);
    }
}
