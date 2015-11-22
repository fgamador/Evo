package fga.evo.model;

import java.util.*;

import static fga.evo.model.Util.sqr;

/**
 * The basic living unit in evo. A circular entity that can move and grow and reproduce.
 * Cells can also bond together to form larger organisms.
 *
 * @author Franz Amador
 */
public class Cell extends Ball implements CellControl.CellApi {
    private double mass;
    private double radius;
    private double area; // cached area derived from radius
    private double energy; // TODO rename as availableEnergy?
    private List<TissueRing> tissueRings = new ArrayList<>();
    private FloatRing floatRing;
    private PhotoRing photoRing;
    private double requestedChildDonation;
    private double donatedEnergy;
    private CellControl control;
    private Cell child;

    public Cell(final double radius) {
        this(radius, c -> {
        });
    }

    public Cell(final double radius, final CellControl control) {
        setRadius(radius);
        tissueRings.add(floatRing = new FloatRing(0, 0));
        tissueRings.add(photoRing = new PhotoRing(radius, floatRing.getArea()));
        this.control = control;
        updateFromRings();
    }

    private void setMass(double val) {
        mass = val;
    }

    private void setRadius(double val) {
        radius = val;
        area = Math.PI * sqr(radius);
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

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
    }

    //=========================================================================
    // Biology
    //=========================================================================

    /**
     * Adds to the cell's available energy.
     */
    public final void addEnergy(final double energy) {
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

    // TODO basic energy-balance idea:
    // 1) start with any energy carried over from last tick
    // 2) add energy from photosynthesis (and absorption/digestion,
    //    which suggests that secreted clouds persist from last tick)
    // 3) subtract energy from tissue maintenance;
    //    we now know our total budget for this tick
    // 4) delegate to control unit (brains), which treats
    //    energy budget as one of its inputs
    // 5) control unit outputs (-inf..inf) signals to tissues,
    //    which cost energy (growth, secretion) or yield energy
    //    (shrinkage); can also donate energy to other bonded
    //    cells (or to whole-organism pool?)
    // 6) if we end up in the black, carry over to next tick
    //    (subject to possible max)
    //    if we end up in the red, we die
    //    (probably will need to start with a lot of cells
    //    so some survive the initial few ticks)

    /**
     * Uses the cell's currently available energy to grow, reproduce, etc.
     */
    public Cell useEnergy() {
        control.allocateEnergy(this);
        balanceEnergy();
        resizeRings();
        return manageChild();
    }

    private void balanceEnergy() {
        double requestedEnergy = Math.max(requestedChildDonation, 0);

        for (TissueRing ring : tissueRings) {
            final double ringRequestedEnergy = ring.getRequestedEnergy();
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

        double innerRadius = 0;
        for (TissueRing ring : tissueRings) {
            ring.updateFromArea(innerRadius);
            innerRadius = ring.getOuterRadius();
        }

        updateFromRings();
    }

    private Cell manageChild() {
        if (requestedChildDonation > 0) {
            if (child == null) {
                return spawn();
            } else {
                child.setDonatedEnergy(requestedChildDonation);
                return null;
            }
        } else if (requestedChildDonation < 0) {
            if (child != null) {
                releaseChild();
            }
            return null;
        } else {
            return null;
        }
    }

    private Cell spawn() {
        child = new Cell(0, control);
        addBond(child);
        child.setDonatedEnergy(requestedChildDonation);
        child.setCenterPosition(getCenterX() + getRadius(), getCenterY()); // TODO random angle
        return child;
    }

    private void releaseChild() {
        removeBond(child);
        child = null;
    }

    /**
     * Returns the cell's current energy budget.
     *
     * @return the total energy available for growth, secretion, donation, thruster, etc.
     */
    public final double getEnergy() {
        return energy;
    }

    private void updateFromRings() {
        setRadius(photoRing.getOuterRadius());
        setMass(floatRing.getMass() + photoRing.getMass());
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

    /**
     * Records a request that the cell's donate a specified amount of energy to its child cell.
     * If it has no child cell, one will be created. If the energy is negative, the child cell
     * will be released to fend for itself.
     *
     * @param donationEnergy the desired amount of energy to donate to the child; negative releases the child
     */
    public void requestChildDonation(double donationEnergy) {
        this.requestedChildDonation = donationEnergy;
    }

    public final double getDonatedEnergy() {
        return donatedEnergy;
    }

    public final void setDonatedEnergy(double val) {
        donatedEnergy = val;
    }

//    final double randomAngle() {
//        return random.nextDouble() * 2 * Math.PI;
//    }

//    private void die() {
//        if (child != null)
//            releaseChild();
//        if (parent != null)
//            parent.releaseChild();
//        thruster = ZeroThruster.INSTANCE;
//        alive = false;
//    }

    public final CellControl getControl() {
        return control;
    }

    public final double getFloatRingOuterRadius() {
        return floatRing.getOuterRadius();
    }

    public final double getPhotoRingOuterRadius() {
        return photoRing.getOuterRadius();
    }

    public final double getFloatArea() {
        return floatRing.getArea();
    }

    public final double getPhotoArea() {
        return photoRing.getArea();
    }

    public final Cell getChild() {
        return child;
    }
}
