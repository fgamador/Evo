package fga.evo.model.geometry;

import fga.evo.model.geometry.Ring;
import fga.evo.model.geometry.RingParameters;
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
