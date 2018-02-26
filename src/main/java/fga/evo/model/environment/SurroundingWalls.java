package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;

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
    public void updateEnvironment(CellEnvironment environment, Cell cell) {
        Ball.addLeftBarrierCollisionForce(cell, environment, minX);
        Ball.addRightBarrierCollisionForce(cell, environment, maxX);
        Ball.addLowBarrierCollisionForce(cell, environment, minY);
        Ball.addHighBarrierCollisionForce(cell, environment, maxY);
    }

    public void resizeWidth(double newWidth) {
        maxX = minX + newWidth;
    }

    public void resizeHeight(double newHeight) {
        minY = maxY - newHeight;
    }
}
