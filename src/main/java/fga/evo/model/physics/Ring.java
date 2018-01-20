package fga.evo.model.physics;

import static fga.evo.model.util.Util.sqr;

public class Ring {
    private RingParameters parameters;
    private double innerRadius;
    private double outerRadius;
    private double area;
    private double mass;

    public Ring(RingParameters parameters, double outerRadius) {
        this.parameters = parameters;
        this.outerRadius = outerRadius;
    }

    public void setArea(double val) {
        area = val;
        outerRadius = 0;
    }

    public void setOuterRadius(double val) {
        outerRadius = val;
        area = 0;
    }

    public void resize(double deltaArea) {
        setArea(getArea() + deltaArea);
    }

    public void syncFields(Ring innerRing) {
        innerRadius = (innerRing != null) ? innerRing.outerRadius : 0;
        if (area != 0) {
            outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        } else {
            outerRadius = Math.max(outerRadius, innerRadius);
            double innerArea = (innerRing != null) ? innerRing.area : 0;
            area = Math.PI * sqr(outerRadius) - innerArea;
        }
        mass = parameters.density.getValue() * area;
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
