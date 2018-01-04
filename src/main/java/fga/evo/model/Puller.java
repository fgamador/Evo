package fga.evo.model;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    static DoubleParameter forceFactor = new DoubleParameter(1);

    private final Cell cell;
    private double x, y;

    public Puller(Cell cell) {
        this.cell = cell;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        cell.addForce(factor * (x - cell.getCenterX()), factor * (y - cell.getCenterY()));
    }
}
