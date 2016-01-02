package fga.evo.model;

import java.util.*;

import static fga.evo.model.Util.sqr;

/**
 * The basic living unit in evo. A circular entity that can subtickPhysics and grow and reproduce.
 * Cells can also bond together to form larger organisms.
 */
public class Cell extends Ball implements CellControl.CellApi {
    private double mass; // cached total ring mass
    private double radius; // cached outer-ring radius
    private double area; // cached area derived from radius
    private double energy; // TODO rename as availableEnergy?
    private double spawnOdds;
    private double releaseChildOdds;
    private double requestedChildDonation;
    private double donatedEnergy;
    private List<TissueRing> tissueRings = new ArrayList<>();
    private FloatRing floatRing;
    private PhotoRing photoRing;
    // TODO move up to Ball
    private DecayingAccumulator overlapAccumulator = new DecayingAccumulator();
    private CellControl control;
    private Cell child;
    private Cell parent;
    private boolean alive = true;

    public Cell(double radius) {
        this(radius, c -> {
        });
    }

    public Cell(double radius, CellControl control) {
        tissueRings.add(floatRing = new FloatRing(0, 0));
        tissueRings.add(photoRing = new PhotoRing(radius, floatRing.getArea()));
        updateFromRings();
        this.control = control;
    }

    //    /** Creates a child cell. */
//    private Cell(Cell parent, double angle) {
//        this(parent.world, ZeroThruster.INSTANCE, CHILD_START_RADIUS, parent.getSpawningX(angle,
//                CHILD_START_RADIUS), parent.getSpawningY(angle, CHILD_START_RADIUS));
//        this.parent = parent;
//    }
//
//    private double getSpawningX(double angle, double childRadius) {
//        return centerX + (radius + childRadius) * Math.cos(angle);
//    }
//
//    private double getSpawningY(double angle, double childRadius) {
//        return centerY + (radius + childRadius) * Math.sin(angle);
//    }

    /**
     * Adds to the cell's available energy.
     */
    public void addEnergy(double energy) {
        this.energy += energy;
    }

    /**
     * Converts incoming light into energy.
     *
     * @param lightIntensity incoming light intensity, as energy per width
     */
    public void photosynthesize(double lightIntensity) {
        addEnergy(photoRing.photosynthesize(lightIntensity));
    }

    public void addDonatedEnergy() {
        addEnergy(donatedEnergy);
        donatedEnergy = 0;
    }

    public void subtractMaintenanceEnergy() {
        for (TissueRing ring : tissueRings) {
            addEnergy(-ring.getMaintenanceEnergy());
        }
    }

    public Cell tickBiology() {
        control.exertControl(this);
        overlapAccumulator.decay();
        return useEnergy();
    }

    private Cell useEnergy() {
        balanceEnergy();
        resizeRings();
        return manageChild();
    }

    private void balanceEnergy() {
        double requestedEnergy = Math.max(requestedChildDonation, 0);

        for (TissueRing ring : tissueRings) {
            double ringRequestedEnergy = ring.getRequestedEnergy();
            if (ringRequestedEnergy > 0) {
                requestedEnergy += ringRequestedEnergy;
            } else {
                energy += -ringRequestedEnergy;
            }
        }

        if (requestedEnergy > energy) {
            requestedChildDonation *= energy / requestedEnergy;
            for (TissueRing ring : tissueRings) {
                if (ring.getRequestedEnergy() > 0) {
                    ring.scaleResizeRequest(energy / requestedEnergy);
                }
            }
        }

        energy -= Math.min(requestedEnergy, energy);
    }

    private void resizeRings() {
        for (TissueRing ring : tissueRings) {
            ring.resize();
        }

        updateFromRingAreas();
    }

    private Cell manageChild() {
        if (requestedChildDonation <= 0) {
            return null;
        }

        if (child != null) {
            child.setDonatedEnergy(requestedChildDonation);
            if (Chance.success(releaseChildOdds)) {
                releaseChild();
            }
            return null;
        }

        if (Chance.success(spawnOdds)) {
            return spawn();
        }

        return null;
    }

    private Cell spawn() {
        child = new Cell(0, control);
        child.parent = this;
        addBond(child);
        child.setDonatedEnergy(requestedChildDonation);
        child.setCenterPosition(getCenterX() + getRadius(), getCenterY()); // TODO random angle
        return child;
    }

    private void releaseChild() {
        removeBond(child);
        child.parent = null;
        child = null;
    }

    void die() {
        alive = false;
        // TODO probably not
        if (child != null) {
            releaseChild();
        } else if (parent != null) {
            parent.releaseChild();
        }
    }

    public void decay() {
        for (TissueRing ring : tissueRings) {
            ring.decay();
        }
        updateFromRingAreas();
    }

    private void updateFromRingAreasOrOuterRadii() {
        updateRingsFromRingAreasOrOuterRadii();
        updateFromRings();
    }

    // TODO syncRings?
    private void updateRingsFromRingAreasOrOuterRadii() {
        TissueRing innerRing = null;
        for (TissueRing ring : tissueRings) {
            ring.updateFromAreaOrOuterRadius(innerRing);
            innerRing = ring;
        }
    }

    private void updateFromRingAreas() {
        updateRingsFromRingAreas();
        updateFromRings();
    }

    private void updateRingsFromRingAreas() {
        double innerRadius = 0;
        for (TissueRing ring : tissueRings) {
            ring.updateFromArea(innerRadius);
            innerRadius = ring.getOuterRadius();
        }
    }

    private void updateFromRings() {
        updateRadiusAndArea();
        updateMass();
    }

    private void updateRadiusAndArea() {
        radius = tissueRings.get(tissueRings.size() - 1).getOuterRadius();
        area = Math.PI * sqr(radius);
    }

    private void updateMass() {
        mass = 0;
        for (TissueRing ring : tissueRings) {
            mass += ring.getMass();
        }
    }

    /**
     * Records a request that the cell's float-ring area grow using a specified amount of energy.
     *
     * @param growthEnergy the desired amount of energy to spend on growth
     */
    public void requestFloatAreaResize(double growthEnergy) {
        floatRing.requestResize(growthEnergy);
    }

    /**
     * Records a request that the cell's photo-ring area grow using a specified amount of energy.
     *
     * @param growthEnergy the desired amount of energy to spend on growth
     */
    public void requestPhotoAreaResize(double growthEnergy) {
        photoRing.requestResize(growthEnergy);
    }

    public void setSpawnOdds(double val) {
        this.spawnOdds = val;
    }

    public void setReleaseChildOdds(double val) {
        this.releaseChildOdds = val;
    }

    /**
     * Records a request that the cell donate a specified amount of energy to its child cell.
     * If it has no child cell, one will be created. If the energy is negative, the child cell
     * will be released to fend for itself.
     *
     * @param donationEnergy the desired amount of energy to donate to the child; negative releases the child
     */
    public void requestChildDonation(double donationEnergy) {
        this.requestedChildDonation = donationEnergy;
    }

    /**
     * Returns the cell's current energy budget.
     *
     * @return the total energy available for growth, secretion, donation, thruster, etc.
     */
    public double getEnergy() {
        return energy;
    }

    public double getDonatedEnergy() {
        return donatedEnergy;
    }

    public void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

//     double randomAngle() {
//        return random.nextDouble() * 2 * Math.PI;
//    }

    public boolean isAlive() {
        return alive;
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

    public double getFloatRingOuterRadius() {
        return floatRing.getOuterRadius();
    }

    public double getPhotoRingOuterRadius() {
        return photoRing.getOuterRadius();
    }

    public double getFloatArea() {
        return floatRing.getArea();
    }

    public double getPhotoArea() {
        return photoRing.getArea();
    }

    // TODO move up to Ball
    @Override
    public void onOverlap(double overlap) {
        overlapAccumulator.addValue(overlap);
    }

    // TODO move up to Ball
    public double getRecentTotalOverlap() {
        return overlapAccumulator.getTotal();
    }

    // TODO lose this; child should release parent instead
    public double getChildNonFloatArea() {
        return (child == null) ? 0 : child.getPhotoArea();
    }

    public CellControl getControl() {
        return control;
    }

    public Cell getChild() {
        return child;
    }

    public Cell getParent() {
        return parent;
    }

    public static class Builder {
        private Cell cell;

        public Builder() {
            cell = new Cell(0);
        }

        public Builder setFloatRingOuterRadius(double radius) {
            cell.floatRing.initOuterRadius(radius);
            return this;
        }

        public Builder setFloatRingArea(double area) {
            cell.floatRing.initArea(area);
            return this;
        }

        public Builder setPhotoRingOuterRadius(double radius) {
            cell.photoRing.initOuterRadius(radius);
            return this;
        }

        public Builder setPhotoRingArea(double area) {
            cell.photoRing.initArea(area);
            return this;
        }

        public Cell build() {
            cell.updateFromRingAreasOrOuterRadii();
            return cell;
        }
    }
}
