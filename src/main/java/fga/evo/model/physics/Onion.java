package fga.evo.model.physics;

import fga.evo.model.geometry.Ring;

import java.util.ArrayList;
import java.util.List;

/**
 * A Ball composed of Rings. Manages ring geometry and mass.
 */
public class Onion extends Ball {
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
            ring.updateRadii(innerRing);
            innerRing = ring;
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
