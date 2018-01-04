package fga.evo.model;

import fga.evo.model.util.DoubleParameter;

public class TestRing extends Ring {
    public static RingParameters parameters = new RingParameters();

    static {
        parameters.density = new DoubleParameter(0.5);
    }

    public TestRing(double outerRadius) {
        super(parameters, outerRadius);
    }

    public TestRing(double outerRadius, Ring innerRing) {
        this(outerRadius);
        syncFields(innerRing);
    }
}
