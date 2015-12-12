package fga.evo.model;

class SimpleBall extends Ball {
    private double mass;
    private double radius;
    private double lastOverlap;

    SimpleBall(double radius) {
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

    @Override
    public void onOverlap(double overlap) {
        lastOverlap = overlap;
    }

    public double getLastOverlap() {
        return lastOverlap;
    }
}
