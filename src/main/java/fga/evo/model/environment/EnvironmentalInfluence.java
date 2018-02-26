package fga.evo.model.environment;

import fga.evo.model.biology.Cell;

/**
 * Something in the cells' environment that can affect them.
 */
public abstract class EnvironmentalInfluence {
    public abstract void updateEnvironment(CellEnvironment environment, Cell cell);

    // TODO make this obsolete
    public final void addEnergyToCell(Cell cell) {
        updateEnvironment(cell.getEnvironment(), cell);
    }
}
