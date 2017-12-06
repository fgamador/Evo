package fga.evo.model.physics;

import fga.evo.model.DoubleParameter;

public class NewtonianBody {
    public static DoubleParameter speedLimit = new DoubleParameter(4);

    protected double mass;
    protected double centerX;
    protected double centerY;
    protected double velocityX;
    protected double velocityY;
    protected double netForceX;
    protected double netForceY;
}
