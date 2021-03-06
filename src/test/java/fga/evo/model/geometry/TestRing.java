package fga.evo.model.geometry;

import fga.evo.model.util.DoubleParameter;

public class TestRing extends Ring {
    public static RingParameters parameters = new RingParameters();

    static {
        parameters.density = new DoubleParameter(0.5);
    }

    public TestRing(double area) {
        super(parameters, area);
    }
}
