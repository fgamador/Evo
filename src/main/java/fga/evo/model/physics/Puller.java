package fga.evo.model.physics;

import fga.evo.model.biology.Cell;
import fga.evo.model.environment.CellEnvironment;
import fga.evo.model.util.DoubleParameter;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    public static DoubleParameter forceFactor = new DoubleParameter(1);

    private final Cell body;
    private double x, y;

    public Puller(Cell body) {
        this.body = body;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        CellEnvironment environment = body.getEnvironment();
        environment.addForce(factor * (x - body.getCenterX()), factor * (y - body.getCenterY()));
    }
}
