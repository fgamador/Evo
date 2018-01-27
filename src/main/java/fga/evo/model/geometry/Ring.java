package fga.evo.model.geometry;

import static fga.evo.model.util.Util.sqr;

public class Ring {
    private RingParameters parameters;
    private double area;
    private double mass;
    private double innerRadius;
    private double outerRadius;

    public Ring(RingParameters parameters, double area) {
        this.parameters = parameters;
        setArea(area);
    }

    public void setArea(double val) {
        area = val;
        mass = parameters.density.getValue() * area;
    }

    public void addToArea(double deltaArea) {
        setArea(getArea() + deltaArea);
    }

    public void setRadiiBasedOnArea(double innerRadius) {
        this.innerRadius = innerRadius;
        outerRadius = Math.sqrt(sqr(this.innerRadius) + area / Math.PI);
    }

    public double getArea() {
        return area;
    }

    public double getMass() {
        return mass;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public double getInnerRadius() {
        return innerRadius;
    }
}
