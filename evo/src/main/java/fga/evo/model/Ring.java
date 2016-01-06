package fga.evo.model;

import static fga.evo.model.Util.sqr;

public class Ring {
    private RingParameters parameters;
    protected double innerRadius; // TODO never gets set!
    protected double outerRadius;
    protected double area;
    protected double mass;

    public Ring(RingParameters parameters, double outerRadius, double innerArea) {
        this.parameters = parameters;
        // TODO do we need these any more?
        this.outerRadius = outerRadius;
        updateFromOuterRadius(innerArea);
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

    // TODO lose this
    public void updateFromArea(double innerRadius) {
        outerRadius = Math.sqrt(sqr(innerRadius) + area / Math.PI);
        mass = parameters.density.getValue() * area;
    }

    // TODO lose this
    private void updateFromOuterRadius(double innerArea) {
        area = Math.PI * sqr(outerRadius) - innerArea;
        mass = parameters.density.getValue() * area;
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
