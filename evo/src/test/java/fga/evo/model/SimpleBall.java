package fga.evo.model;

/**
 * Created by Franz on 11/22/2015.
 */
class SimpleBall extends Ball {
    private double mass;
    private double radius;

    SimpleBall(double radius) {
        setRadius(radius);
    }

    public void setMass(double val) {
        mass = val;
    }

    public void setRadius(double val) {
        radius = val;
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }
}
