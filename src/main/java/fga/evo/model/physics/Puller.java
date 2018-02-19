package fga.evo.model.physics;

import fga.evo.model.util.DoubleParameter;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    public static DoubleParameter forceFactor = new DoubleParameter(1);

    private final NewtonianBody body;
    private double x, y;

    public Puller(NewtonianBody body) {
        this.body = body;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        body.getForces().addForce(factor * (x - body.getCenterX()), factor * (y - body.getCenterY()));
    }
}
