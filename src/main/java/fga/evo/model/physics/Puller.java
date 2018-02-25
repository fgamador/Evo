package fga.evo.model.physics;

import fga.evo.model.util.DoubleParameter;

/**
 * Created when the user's mouse cursor drags a cell.
 */
public class Puller {
    public static DoubleParameter forceFactor = new DoubleParameter(1);

    private double x, y;
    private NewtonianBody pulledBody;
    private NewtonianBodyEnvironment pulledBodyEnvironment;

    public Puller(NewtonianBodyEnvironment pulledBodyEnvironment, NewtonianBody pulledBody) {
        this.pulledBody = pulledBody;
        this.pulledBodyEnvironment = pulledBodyEnvironment;
    }

    public Puller(NewtonianBodyEnvironment pulledBodyEnvironment) {
        this.pulledBodyEnvironment = pulledBodyEnvironment;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addForce() {
        double factor = forceFactor.getValue();
        pulledBodyEnvironment.addForce(factor * (x - pulledBodyEnvironment.getCenterX()), factor * (y - pulledBodyEnvironment.getCenterY()));
    }
}
