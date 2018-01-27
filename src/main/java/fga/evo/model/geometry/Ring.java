package fga.evo.model.geometry;

import static fga.evo.model.util.Util.sqr;

public class Ring {
    private RingParameters parameters;
    private double innerRadius;
    private double outerRadius;
    private double area;
    private double mass;

    public Ring(RingParameters parameters, double area) {
        this.parameters = parameters;
        setArea(area);
    }

    public void setArea(double val) {
        area = val;
        outerRadius = 0;
        mass = parameters.density.getValue() * area;
    }

    public void resize(double deltaArea) {
        setArea(getArea() + deltaArea);
    }

    public void updateRadii(Ring innerRing) {
        updateRadii((innerRing != null) ? innerRing.outerRadius : 0);
    }

    public void updateRadii(double innerRingRadius) {
        innerRadius = innerRingRadius;
        outerRadius = Math.sqrt(sqr(this.innerRadius) + area / Math.PI);
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public double getArea() {
        return area;
    }

    public double getMass() {
        return mass;
    }
}
