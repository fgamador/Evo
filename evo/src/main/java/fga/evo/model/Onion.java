package fga.evo.model;

import java.util.ArrayList;
import java.util.List;

import static fga.evo.model.Util.sqr;

/**
 * A Ball composed of Rings. Manages ring geometry and mass.
 */
public abstract class Onion extends Ball {
    private double radius; // cached outer-ring radius
    private double area; // cached area derived from radius
    private double mass; // cached total ring mass
    private List<Ring> rings = new ArrayList<>();

    protected void addRing(Ring ring) {
        rings.add(ring);
    }

    protected void syncFields() {
        syncRingFields();
        updateFromRings();
    }

    private void syncRingFields() {
        Ring innerRing = null;
        for (Ring ring : rings) {
            ring.syncFields(innerRing);
            innerRing = ring;
        }
    }

    private void updateFromRings() {
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

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
    }

    public double getMass() {
        return mass;
    }
}
