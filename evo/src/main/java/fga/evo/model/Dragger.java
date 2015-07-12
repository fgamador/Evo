package fga.evo.model;

/**
 * Represents the user's mouse cursor dragging a cell.
 *
 * @author Franz Amador
 */
public class Dragger {
    private static double dragForceFactor = 1;

    private final Cell cell;
    private double x, y;

    public Dragger(Cell cell) {
        this.cell = cell;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForceToCell() {
        cell.addForce(dragForceFactor * (x - cell.getCenterX()), dragForceFactor * (y - cell.getCenterY()));
    }

    public static double getDragForceFactor() {
        return dragForceFactor;
    }

    public static void setDragForceFactor(final double val) {
        dragForceFactor = val;
    }
}
