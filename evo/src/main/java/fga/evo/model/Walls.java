package fga.evo.model;

/**
 * The set of four walls surrounding the cells.
 *
 * @author Franz Amador
 */
public class Walls extends EnvironmentalInfluence {
    private double width, height;

    public Walls(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public final void addForcesToCell(final Cell cell) {
        cell.addForce(cell.calcLowXWallCollisionForce(0), 0);
        cell.addForce(cell.calcHighXWallCollisionForce(width), 0);
        cell.addForce(0, cell.calcLowYWallCollisionForce(0));
        cell.addForce(0, cell.calcHighYWallCollisionForce(height));
    }
}
