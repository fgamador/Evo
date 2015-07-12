package fga.evo.model;

/**
 * The set of four walls surrounding the cells.
 *
 * @author Franz Amador
 */
public class Box {
    private double width, height;

    public Box(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    public final void addWallCollisionForcesToCell(final Cell cell) {
        cell.addForce(cell.calcLowXWallCollisionForce(0), 0);
        cell.addForce(cell.calcHighXWallCollisionForce(width), 0);
        cell.addForce(0, cell.calcLowYWallCollisionForce(0));
        cell.addForce(0, cell.calcHighYWallCollisionForce(height));
    }
}
