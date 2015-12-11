package fga.evo.model;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    private static double pullForceFactor = 1;

    private final Cell cell;
    private double x, y;

    public Puller(Cell cell) {
        this.cell = cell;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForceToCell() {
        cell.addForce(pullForceFactor * (x - cell.getCenterX()), pullForceFactor * (y - cell.getCenterY()));
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getPullForceFactor() {
        return pullForceFactor;
    }

    public static void setPullForceFactor(final double val) {
        pullForceFactor = val;
    }
}
