package fga.evo.model;

/**
 * Something in the cells' environment that can affect them.
 * TODO split this into force influence/source and energy influence/source
 */
public abstract class EnvironmentalInfluence {
    public void addEnergyToCell(Cell cell) {
    }

    public void addForcesToCell(Ball cell) {
    }
}
