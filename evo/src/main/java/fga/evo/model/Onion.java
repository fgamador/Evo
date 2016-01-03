package fga.evo.model;

import java.util.ArrayList;
import java.util.List;

import static fga.evo.model.Util.sqr;

public abstract class Onion extends Ball {
    protected double mass; // cached total ring mass
    protected double radius; // cached outer-ring radius
    protected double area; // cached area derived from radius
    private List<Ring> rings = new ArrayList<>();

    protected void addRing(Ring ring) {
        rings.add(ring);
    }

    protected void updateFromRingAreasOrOuterRadii() {
        updateRingsFromRingAreasOrOuterRadii();
        updateFromRings();
    }

    // TODO syncRings?
    private void updateRingsFromRingAreasOrOuterRadii() {
        Ring innerRing = null;
        for (Ring ring : rings) {
            ring.updateFromAreaOrOuterRadius(innerRing);
            innerRing = ring;
        }
    }

    protected void updateFromRingAreas() {
        updateRingsFromRingAreas();
        updateFromRings();
    }

    private void updateRingsFromRingAreas() {
        double innerRadius = 0;
        for (Ring ring : rings) {
            ring.updateFromArea(innerRadius);
            innerRadius = ring.getOuterRadius();
        }
    }

    protected void updateFromRings() {
        updateRadiusAndArea();
        updateMass();
    }

    private void updateRadiusAndArea() {
        radius = rings.get(rings.size() - 1).getOuterRadius();
        area = Math.PI * sqr(radius);
    }

    private void updateMass() {
        mass = 0;
        for (Ring ring : rings) {
            mass += ring.getMass();
        }
    }

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
    }
}
