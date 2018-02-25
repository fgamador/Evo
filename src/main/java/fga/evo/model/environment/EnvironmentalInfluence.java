package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;

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
    public void addForcesToCell(Ball cell) {
        //updateEnvironment(cell.getEnvironment(), cell);
    }
}
