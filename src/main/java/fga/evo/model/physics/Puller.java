package fga.evo.model.physics;

import fga.evo.model.environment.CellEnvironment;
import fga.evo.model.util.DoubleParameter;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    public static DoubleParameter forceFactor = new DoubleParameter(1);

    private double x, y;
    private CellEnvironment environment;

    public Puller(CellEnvironment environment) {
        this.environment = environment;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        environment.addForce(factor * (x - environment.getCenterX()), factor * (y - environment.getCenterY()));
    }
}
