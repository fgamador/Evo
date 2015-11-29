package fga.evo.model;

import java.util.*;

import static fga.evo.model.Util.sqr;

/**
 * The basic living unit in evo. A circular entity that can move and grow and reproduce.
 * Cells can also bond together to form larger organisms.
 *
 * @author Franz Amador
 */
public class Cell implements CellControl.CellApi {
    private static double speedLimit = 4;
    private static double overlapForceFactor = 1;

    private Set<Cell> bondedCells = new HashSet<>();
    private Cell child;
    private double mass; // cached sum of ring masses
    private double radius; // cached outer radius of outer ring
    private double area; // cached sum of ring areas
    private double centerX, centerY;
    private double velocityX, velocityY;
    private double netForceX, netForceY;
    private double energy; // TODO rename as availableEnergy?
    private List<TissueRing> tissueRings = new ArrayList<>();
    private FloatRing floatRing;
    private PhotoRing photoRing;
    private CellControl control;
    private double requestedChildDonation;
    private double donatedEnergy;

    public Cell(final double radius) {
        this(radius, c -> {
        });
    }

    public Cell(final double radius, final CellControl control) {
        tissueRings.add(floatRing = new FloatRing(0, 0));
        tissueRings.add(photoRing = new PhotoRing(radius, floatRing.getArea()));
        this.control = control;
        updateFromRings();
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

    public final void addBond(Cell cell2) {
        bondedCells.add(cell2);
        cell2.bondedCells.add(this);
    }

    public final void removeBond(Cell cell2) {
        bondedCells.remove(cell2);
        cell2.bondedCells.remove(this);
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
        child.setPosition(centerX + radius, centerY); // TODO random angle
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
        radius = photoRing.getOuterRadius();
        mass = floatRing.getMass() + photoRing.getMass();
        area = Math.PI * sqr(radius);
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

    //=========================================================================
    // Physics
    //=========================================================================

    /**
     * Sets the cell's initial position. All subsequent updates to position should be done by {@link #move()}.
     */
    public final void setPosition(final double centerX, final double centerY) {
        assert centerX >= 0;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Sets the cell's initial velocity. All subsequent updates to velocity should be done by {@link #move()}.
     */
    public final void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Adds a force on the cell that will be used by the next call to {@link #move()}. This is the only way to
     * influence the cell's motion (after setting its initial position and possibly velocity).
     *
     * @param x X-component of the force
     * @param y Y-component of the force
     */
    public final void addForce(final double x, final double y) {
        netForceX += x;
        netForceY += y;
    }

    /**
     * Updates the cell's velocity and position per the forces currently on it, then clears the forces.
     */
    public final void move() {
        // the acceleration to apply instantaneously at the beginning this time interval
        final double accelerationX = netForceX / mass;
        final double accelerationY = netForceY / mass;

        // the velocity during this time interval
        velocityX += accelerationX;
        velocityY += accelerationY;

        // TODO simpler check before doing this one? e.g. abs(vx) + abs(vy) > max/2?
        // numerical/discretization problems can cause extreme velocities; cap them
        final double speedSquared = sqr(velocityX) + sqr(velocityY);
        if (speedSquared > sqr(speedLimit)) {
            final double throttling = speedLimit / Math.sqrt(speedSquared);
            velocityX *= throttling;
            velocityY *= throttling;
        }

        // the position at the end of this time interval
        centerX += velocityX;
        centerY += velocityY;

        // clear the forces
        netForceX = netForceY = 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its left (smaller x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcMinXWallCollisionForce(final double wallX) {
        final double overlap = radius - (centerX - wallX);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall to its right (larger x position).
     *
     * @param wallX x-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcMaxXWallCollisionForce(final double wallX) {
        final double overlap = centerX + radius - wallX;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall below it (smaller y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcMinYWallCollisionForce(final double wallY) {
        final double overlap = radius - (centerY - wallY);
        return (overlap > 0) ? calcOverlapForce(overlap) : 0;
    }

    /**
     * Returns the force exerted on the cell if it is in collision with a wall above it (larger y position).
     *
     * @param wallY y-position of the wall
     * @return the collision force or zero if not in collision
     */
    public final double calcMaxYWallCollisionForce(final double wallY) {
        final double overlap = centerY + radius - wallY;
        return (overlap > 0) ? -calcOverlapForce(overlap) : 0;
    }

    /**
     * Adds the forces due to the interaction of this cell with another cell, such as a collision or a bond.
     * Updates the forces on both of the cells. Call this only once for any particular pair of cells.
     *
     * @param cell2 the other cell
     */
    public void addInterCellForces(final Cell cell2) {
        final double relativeCenterX = centerX - cell2.centerX;
        final double relativeCenterY = centerY - cell2.centerY;
        final double centerSeparation = Math.sqrt(sqr(relativeCenterX) + sqr(relativeCenterY));

        if (centerSeparation != 0) {
            if (bondedCells.contains(cell2)) {
                addBondForces(cell2, relativeCenterX, relativeCenterY, centerSeparation);
            } else {
                addCollisionForces(cell2, relativeCenterX, relativeCenterY, centerSeparation);
            }
        }
    }

    /**
     * Adds forces to the cells that will move them back toward just touching one another.
     */
    private void addBondForces(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
        final double overlap = radius + cell2.radius - centerSeparation;
        addOverlapForces(cell2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
    }

    /**
     * Experimental. Adds forces to the cells that, in the absence of other forces, will restore the
     * gap/overlap to zero on the next call to {@link #move()}.
     */
//    private void addBondForces2(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
//        final double relativeVelocityX = velocityX - cell2.velocityX;
//        final double relativeVelocityY = velocityY - cell2.velocityY;
//        final double compressionFactor = ((radius + cell2.radius) / centerSeparation) - 1;
//        final double massFactor = (1 / mass) + (1 / cell2.mass);
//        final double forceX = ((compressionFactor * relativeCenterX) - relativeVelocityX) / massFactor;
//        final double forceY = ((compressionFactor * relativeCenterY) - relativeVelocityY) / massFactor;
//        addForce(forceX, forceY);
//        cell2.addForce(-forceX, -forceY);
//    }

    /**
     * Adds forces to the cells that will push them away from one another.
     */
    private void addCollisionForces(final Cell cell2, final double relativeCenterX, final double relativeCenterY, final double centerSeparation) {
        final double overlap = radius + cell2.radius - centerSeparation;
        if (overlap > 0) {
            addOverlapForces(cell2, relativeCenterX, relativeCenterY, centerSeparation, overlap);
        }
    }

    private void addOverlapForces(Cell cell2, double relativeCenterX, double relativeCenterY, double centerSeparation, double overlap) {
        final double force = calcOverlapForce(overlap);
        final double forceX = (relativeCenterX / centerSeparation) * force;
        final double forceY = (relativeCenterY / centerSeparation) * force;
        addForce(forceX, forceY);
        cell2.addForce(-forceX, -forceY);
    }

    public static double calcOverlapForce(final double overlap) {
        return overlapForceFactor * overlap;
    }

    public final double getMass() {
        return mass;
    }

    public final double getRadius() {
        return radius;
    }

    public double getArea() {
        return area;
    }

    public final double getCenterX() {
        return centerX;
    }

    public final double getCenterY() {
        return centerY;
    }

    public final double getVelocityX() {
        return velocityX;
    }

    public final double getVelocityY() {
        return velocityY;
    }

    public final double getNetForceX() {
        return netForceX;
    }

    public final double getNetForceY() {
        return netForceY;
    }

    public final Set<Cell> getBondedCells() {
        return Collections.unmodifiableSet(bondedCells);
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getSpeedLimit() {
        return speedLimit;
    }

    public static void setSpeedLimit(final double val) {
        speedLimit = val;
    }

    public static double getOverlapForceFactor() {
        return overlapForceFactor;
    }

    public static void setOverlapForceFactor(final double val) {
        overlapForceFactor = val;
    }
}
