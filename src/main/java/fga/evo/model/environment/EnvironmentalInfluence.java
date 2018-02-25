package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;

/**
 * Something in the cells' environment that can affect them.
 * TODO split this into force influence/source and energy influence/source
 */
public abstract class EnvironmentalInfluence {
    public abstract void updateEnvironment(CellEnvironment environment);

    // TODO make this obsolete
    public void addEnergyToCell(Cell cell) {
    }

    // TODO make this obsolete
    public void addForcesToCell(Ball cell) {
    }
}
