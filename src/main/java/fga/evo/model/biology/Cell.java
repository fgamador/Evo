package fga.evo.model.biology;

import fga.evo.model.control.CellControl;
import fga.evo.model.physics.Onion;
import fga.evo.model.physics.PairBond;
import fga.evo.model.geometry.Ring;
import fga.evo.model.util.Chance;
import fga.evo.model.util.DoubleParameter;

import java.util.*;

/**
 * The basic living unit in evo. A circular entity that can move and grow and reproduce.
 * Cells can also bond together to form larger organisms.
 */
public class Cell extends Onion implements CellControl.CellApi {
    public static DoubleParameter maximumSurvivableDamage = new DoubleParameter(10);

    private static final State ALIVE = new Alive();
    private static final State DEAD = new Dead();

    private List<TissueRing> tissueRings = new ArrayList<>();
    private FloatRing floatRing;
    private PhotoRing photoRing;
    private CellControl control;
    private State state = ALIVE;
    private double energy;
    private double spawnOdds;
    private double releaseChildOdds;
    private double releaseParentOdds;
    private double requestedChildDonation;
    private double donatedEnergy;
    private Cell child;
    private Cell parent;
    private double damage;
    private CellLifecycleListener lifecycleListener;

    public Cell(double radius) {
        this(radius, c -> {
        });
    }

    public Cell(double radius, CellControl control) {
        this(radius, control, CellLifecycleListener.NULL_LISTENER);
    }

    public Cell(double radius, CellControl control, CellLifecycleListener lifecycleListener) {
        addRing(floatRing = new FloatRing(0));
        addRing(photoRing = new PhotoRing(radius));
        syncFields();
        this.control = control;
        this.lifecycleListener = lifecycleListener;
    }

    protected void addRing(TissueRing ring) {
        super.addRing(ring);
        tissueRings.add(ring);
    }

    /**
     * Converts incoming light into energy.
     *
     * @param lightIntensity incoming light intensity, as energy per width
     */
    public void photosynthesize(double lightIntensity) {
        addEnergy(photoRing.photosynthesize(lightIntensity));
    }

    public void die() {
        state = DEAD;
        lifecycleListener.onCellDied(this);
    }

    public void tickBiology_ControlPhase() {
        state.controlPhase(this);
    }

    public void tickBiology_ConsequencesPhase() {
        state.consequencesPhase(this);
    }

    private void liveControlPhase() {
        control.exertControl(this);
        adjustAndChargeForEnergyRequests();
        resizeRings();
        manageParent();
        manageChild();
    }

    private void adjustAndChargeForEnergyRequests() {
        double intendedEnergyConsumption = Math.max(requestedChildDonation, 0);

        for (TissueRing ring : tissueRings) {
            double ringIntendedEnergyConsumption = ring.getIntendedEnergyConsumption();
            if (ringIntendedEnergyConsumption > 0) {
                intendedEnergyConsumption += ringIntendedEnergyConsumption;
            } else {
                energy += -ringIntendedEnergyConsumption;
            }
        }

        if (intendedEnergyConsumption > energy) {
            requestedChildDonation *= energy / intendedEnergyConsumption;
            for (TissueRing ring : tissueRings) {
                if (ring.getIntendedEnergyConsumption() > 0) {
                    ring.scaleResizeRequest(energy / intendedEnergyConsumption);
                }
            }
        }

        energy -= Math.min(intendedEnergyConsumption, energy);
    }

    private void resizeRings() {
        for (TissueRing ring : tissueRings) {
            ring.resize();
        }
        syncFields();
    }

    private void manageParent() {
        if (parent != null) {
            if (Chance.beats(releaseParentOdds)) {
                parent.releaseChild();
            }
        }
    }

    private void manageChild() {
        if (requestedChildDonation <= 0) {
            return;
        }

        if (child != null) {
            child.setDonatedEnergy(requestedChildDonation);
            if (Chance.beats(releaseChildOdds)) {
                releaseChild();
            }
            return;
        }

        if (Chance.beats(spawnOdds)) {
            spawn();
        }
    }

    private void spawn() {
        child = new Cell(0, control, lifecycleListener);
        child.parent = this;
        PairBond bond = addBond(child);
        child.addEnergy(requestedChildDonation);
        child.setCenterPosition(getCenterX() + getRadius(), getCenterY()); // TODO random angle
        lifecycleListener.onCellBorn(child);
        lifecycleListener.onBondFormed(bond);
    }

//    private double getSpawningX(double angle, double childRadius) {
//        return centerX + (radius + childRadius) * Math.cos(angle);
//    }
//    private double getSpawningY(double angle, double childRadius) {
//        return centerY + (radius + childRadius) * Math.sin(angle);
//    }
//    double randomAngle() {
//        return random.nextDouble() * 2 * Math.PI;
//    }

    private void releaseChild() {
        PairBond bond = removeBond(child);
        child.parent = null;
        child = null;
        lifecycleListener.onBondBroken(bond);
    }

    private void liveConsequencesPhase() {
        addDonatedEnergy();
        subtractMaintenanceEnergy();
        addDamage();
    }

    private void addDonatedEnergy() {
        addEnergy(donatedEnergy);
        donatedEnergy = 0;
    }

    private void subtractMaintenanceEnergy() {
        for (TissueRing ring : tissueRings) {
            addEnergy(-ring.getMaintenanceEnergy());
        }
    }

    private void addEnergy(double energy) {
        this.energy += energy;
    }

    private void addDamage() {
        if (energy < 0) {
            damage -= energy;
        }
        if (damage > maximumSurvivableDamage.getValue()) {
            die();
        }
    }

    private void deadControlPhase() {
    }

    private void deadConsequencesPhase() {
        decay();
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

    public void setReleaseParentOdds(double val) {
        this.releaseParentOdds = val;
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

    public boolean isAlive() {
        return state == ALIVE;
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

    public double getNonFloatArea() {
        return getArea() - getFloatArea();
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

    public double getDamage() {
        return damage;
    }

    public CellLifecycleListener getLifecycleListener() {
        return lifecycleListener;
    }

    public void setLifecycleListener(CellLifecycleListener listener) {
        this.lifecycleListener = listener;
    }

    private interface State {
        void controlPhase(Cell cell);

        void consequencesPhase(Cell cell);
    }

    private static class Alive implements State {
        public void controlPhase(Cell cell) {
            cell.liveControlPhase();
        }

        public void consequencesPhase(Cell cell) {
            cell.liveConsequencesPhase();
        }
    }

    private static class Dead implements State {
        public void controlPhase(Cell cell) {
            cell.deadControlPhase();
        }

        public void consequencesPhase(Cell cell) {
            cell.deadConsequencesPhase();
        }
    }

    public static class Builder {
        private Cell cell;
        private double energy;

        public Builder() {
            cell = new Cell(0);
        }

        public Builder setControl(CellControl control) {
            cell.control = control;
            return this;
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

        public Builder setEnergy(double energy) {
            cell.energy = energy;
            return this;
        }

        public Builder setLifecycleListener(CellLifecycleListener listener) {
            cell.lifecycleListener = listener;
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
