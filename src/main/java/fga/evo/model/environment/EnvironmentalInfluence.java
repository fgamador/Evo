package fga.evo.model.environment;

import fga.evo.model.biology.Cell;

/**
 * Something in the cells' environment that can affect them.
 */
public interface EnvironmentalInfluence {
    void updateEnvironment(Cell cell);
}
