package fga.evo.model;

/**
 * The set of four walls surrounding the cells.
 *
 * @author Franz Amador
 */
public class Walls extends EnvironmentalInfluence {
    private double minX, maxX, minY, maxY;

    public Walls(double minX, final double maxX, final double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public final void addForcesToCell(final Cell cell) {
        cell.addForce(BallForces.calcMinXWallCollisionForce(cell, minX), 0);
        cell.addForce(BallForces.calcMaxXWallCollisionForce(cell, maxX), 0);
        cell.addForce(0, BallForces.calcMinYWallCollisionForce(cell, minY));
        cell.addForce(0, BallForces.calcMaxYWallCollisionForce(cell, maxY));
    }
}
