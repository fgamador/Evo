package fga.evo.model;

import java.util.*;

/**
 * The basic living unit in evo. A circular entity that can move and grow and reproduce.
 * Cells can also bond together to form larger organisms.
 */
public class Cell extends Onion implements CellControl.CellApi {
    private List<TissueRing> tissueRings = new ArrayList<>();
    private FloatRing floatRing;
    private PhotoRing photoRing;
    private CellControl control;
    private boolean alive = true;
    private double energy;
    private double spawnOdds;
    private double releaseChildOdds;
    private double requestedChildDonation;
    private double donatedEnergy;
    private Cell child;
    private Cell parent;

    public Cell(double radius) {
        this(radius, c -> {
        });
    }

    public Cell(double radius, CellControl control) {
        addRing(floatRing = new FloatRing(0));
        addRing(photoRing = new PhotoRing(radius));
        syncFields();
        this.control = control;
    }

    protected void addRing(TissueRing ring) {
        super.addRing(ring);
        tissueRings.add(ring);
    }

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
        syncFields();
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
        syncFields();
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
            setOuterRadius(cell.floatRing, radius);
            return this;
        }

        public Builder setFloatRingArea(double area) {
            setArea(cell.floatRing, area);
            return this;
        }

        public Builder setPhotoRingOuterRadius(double radius) {
            setOuterRadius(cell.photoRing, radius);
            return this;
        }

        public Builder setPhotoRingArea(double area) {
            setArea(cell.photoRing, area);
            return this;
        }

        public Cell build() {
            cell.syncFields();
            return cell;
        }

        private void setArea(Ring ring, double val) {
            enforceUnsetAreaAndOuterRadius(ring);
            ring.setArea(val);
        }

        private void setOuterRadius(Ring ring, double val) {
            enforceUnsetAreaAndOuterRadius(ring);
            ring.setOuterRadius(val);
        }

        private void enforceUnsetAreaAndOuterRadius(Ring ring) {
            if (ring.getArea() != 0) {
                throw new IllegalStateException("Area is already set to " + ring.getArea());
            }
            if (ring.getOuterRadius() != 0) {
                throw new IllegalStateException("Outer radius is already set to " + ring.getOuterRadius());
            }
        }
    }
}
