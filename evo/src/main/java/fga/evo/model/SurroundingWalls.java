package fga.evo.model;

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
    public void addForcesToCell(final Cell cell) {
        cell.addForce(BallForces.calcLeftBarrierCollisionForce(cell, minX), 0);
        cell.addForce(BallForces.calcRightBarrierCollisionForce(cell, maxX), 0);
        cell.addForce(0, BallForces.calcLowBarrierCollisionForce(cell, minY));
        cell.addForce(0, BallForces.calcHighBarrierCollisionForce(cell, maxY));
    }
}
