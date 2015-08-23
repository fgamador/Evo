package fga.evo.model;

/**
 * The set of four walls surrounding the cells.
 *
 * @author Franz Amador
 */
public class Walls extends EnvironmentalInfluence {
    private double width, depth;

    public Walls(final double width, final double depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.width = width;
        this.depth = depth;
    }

    @Override
    public final void addForcesToCell(final Cell cell) {
        cell.addForce(cell.calcLowXWallCollisionForce(0), 0);
        cell.addForce(cell.calcHighXWallCollisionForce(width), 0);
        cell.addForce(0, cell.calcHighYWallCollisionForce(0));
        cell.addForce(0, cell.calcLowYWallCollisionForce(-depth));
    }
}
