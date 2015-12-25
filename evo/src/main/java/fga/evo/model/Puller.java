package fga.evo.model;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    static DoubleParameter pullerForceFactor = new DoubleParameter(1).register("pullerForceFactor");

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
        double forceFactor = pullerForceFactor.getValue();
        cell.addForce(forceFactor * (x - cell.getCenterX()), forceFactor * (y - cell.getCenterY()));
    }
}
