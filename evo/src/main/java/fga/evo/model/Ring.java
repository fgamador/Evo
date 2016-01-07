package fga.evo.model;

import static fga.evo.model.Util.sqr;

public class Ring {
    private RingParameters parameters;
    protected double innerRadius; // TODO never gets set!
    private double outerRadius;
    private double area;
    private double mass;

    public Ring(RingParameters parameters, double outerRadius) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
    }

    public void initArea(double area) {
        enforceUnsetAreaAndOuterRadius();
        this.area = area;
    }

    public void initOuterRadius(double radius) {
        enforceUnsetAreaAndOuterRadius();
        this.outerRadius = radius;
    }

    private void enforceUnsetAreaAndOuterRadius() {
        if (this.area != 0) {
            throw new IllegalStateException("Area is already set to " + this.area);
        }
        if (this.outerRadius != 0) {
            throw new IllegalStateException("Outer radius is already set to " + this.outerRadius);
        }
    }

    public void syncFields(Ring innerRing) {
        double innerRadius = (innerRing != null) ? innerRing.outerRadius : 0;
        if (area != 0) {
            outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        } else {
            outerRadius = Math.max(outerRadius, innerRadius);
            double innerArea = (innerRing != null) ? innerRing.area : 0;
            area = Math.PI * sqr(outerRadius) - innerArea;
        }
        mass = parameters.density.getValue() * area;
    }

    public void setArea(double val) {
        area = val;
        outerRadius = 0;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public double getArea() {
        return area;
    }

    public double getMass() {
        return mass;
    }
}
