package fga.evo.model;

/**
 * Something in the cells' environment that can affect them.
 */
public abstract class EnvironmentalInfluence {
    public void addEnergyToCell(final Cell cell) { }

    public void addForcesToCell(final Cell cell) { }
}
