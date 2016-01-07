package fga.evo.model;

public class TestRing extends Ring {
    public static RingParameters parameters = new RingParameters();

    static {
        parameters.density = new DoubleParameter(0.5);
    }

    public TestRing(double outerRadius) {
        super(parameters, outerRadius);
    }
}
