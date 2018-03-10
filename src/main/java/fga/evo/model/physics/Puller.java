package fga.evo.model.physics;

import fga.evo.model.util.DoubleParameter;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    public static DoubleParameter forceFactor = new DoubleParameter(1);

    private double x, y;
    private NewtonianBody pulledBody;

    public Puller(NewtonianBody pulledBody) {
        this.pulledBody = pulledBody;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        pulledBody.getEnvironment().addForce(factor * (x - pulledBody.getCenterX()), factor * (y - pulledBody.getCenterY()));
    }
}
