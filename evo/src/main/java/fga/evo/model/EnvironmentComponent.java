package fga.evo.model;

/**
 * Something in the cells' environment that can affect them.
 *
 * @author Franz Amador
 */
public abstract class EnvironmentComponent {
    public void addEnergyToCell(final Cell cell) { }

    public void addForcesToCell(final Cell cell) { }
}
