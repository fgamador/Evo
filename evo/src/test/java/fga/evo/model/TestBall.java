package fga.evo.model;

class TestBall extends Ball {
    private double mass;
    private double radius;
    private double lastOverlap;

    TestBall(double radius) {
        setRadius(radius);
    }

    public void setMass(double val) {
        mass = val;
    }

    public void setRadius(double val) {
        radius = val;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public double getRadius() {
        return radius;
    }
}
