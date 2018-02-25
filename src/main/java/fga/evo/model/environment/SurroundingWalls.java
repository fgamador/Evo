package fga.evo.model.environment;

import fga.evo.model.biology.Cell;

/**
 * The four walls surrounding the cells.
 */
public class SurroundingWalls extends EnvironmentalInfluence {
    private double minX, maxX, minY, maxY;

    public SurroundingWalls(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public void addForcesToCell(Cell cell) {
        cell.addLeftBarrierCollisionForce(minX);
        cell.addRightBarrierCollisionForce(maxX);
        cell.addLowBarrierCollisionForce(minY);
        cell.addHighBarrierCollisionForce(maxY);
    }

    // TODO new API
    @Override
    public void updateEnvironment(CellEnvironment environment, Cell cell) {
    }

    public void resizeWidth(double newWidth) {
        maxX = minX + newWidth;
    }

    public void resizeHeight(double newHeight) {
        minY = maxY - newHeight;
    }
}
