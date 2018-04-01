package fga.evo.model.physics;

import fga.evo.model.geometry.Ring;

import java.util.ArrayList;
import java.util.List;

/**
 * A Ball composed of Rings. Manages ring geometry and mass.
 */
public abstract class Onion<T extends Ring> extends Ball {
    private List<T> rings = new ArrayList<>();

    protected void addRing(T ring) {
        rings.add(ring);
    }

    protected List<T> getRings() {
        return rings;
    }

    protected void syncFields() {
        syncRingFields();
        updateFromRings();
    }

    private void syncRingFields() {
        double innerRadius = 0;
        for (Ring ring : rings) {
            ring.setRadiiBasedOnArea(innerRadius);
            innerRadius = ring.getOuterRadius();
        }
    }

    private void updateFromRings() {
        updateRadiusAndArea();
        updateMass();
    }

    private void updateRadiusAndArea() {
        setRadius(rings.get(rings.size() - 1).getOuterRadius());
    }

    private void updateMass() {
        setMass(0);
        for (Ring ring : rings) {
            setMass(getMass() + ring.getMass());
        }
    }
}
