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
        BallForces.addLeftBarrierCollisionForce(cell, minX);
        BallForces.addRightBarrierCollisionForce(cell, maxX);
        BallForces.addLowBarrierCollisionForce(cell, minY);
        BallForces.addHighBarrierCollisionForce(cell, maxY);
    }
}
