package fga.evo.model;

import fga.evo.model.biology.AccumulatingCellLifecycleListener;
import fga.evo.model.biology.Cell;
import fga.evo.model.environment.EnvironmentalInfluence;
import fga.evo.model.environment.Illumination;
import fga.evo.model.geometry.OverlapDetection;
import fga.evo.model.physics.PairBond;
import fga.evo.model.physics.Puller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The world in which the cells live. The root container of the whole model. The entry point for simulation clock ticks.
 */
public class World {
    private static int subticksPerTick = 2;

    private List<EnvironmentalInfluence> environmentalInfluences = new ArrayList<>();
    private List<EnvironmentalInfluence> energyInfluences = new ArrayList<>();
    private List<EnvironmentalInfluence> forceInfluences = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<PairBond> bonds = new ArrayList<>();
    private Puller puller;
    private AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
    private OverlapDetection overlapDetection = new OverlapDetection();

    public void addEnvironmentalInfluence(EnvironmentalInfluence influence) {
        environmentalInfluences.add(influence);
        // TODO bleah. Temp until can unify update-environment into one phase.
        if (influence instanceof Illumination)
            energyInfluences.add(influence);
        else
            forceInfluences.add(influence);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
        overlapDetection.addCircle(cell);
        cell.setLifecycleListener(lifecycleListener);
    }

    /**
     * Propagates a simulation clock tick through the model.
     */
    public Collection<Cell> tick() {
        lifecycleListener.clear();
        tickWithMonitoredLifecycles();
        updatePerLifecycleChanges();
        return lifecycleListener.bornCells;
    }

    private void tickWithMonitoredLifecycles() {
        for (int i = 0; i < subticksPerTick; i++) {
            updateCellEnvironments();

            for (Cell cell : cells)
                cell.subtickPhysics(subticksPerTick);
        }

        for (Cell cell : cells)
            cell.tickBiology_ConsequencesPhase();

        for (Cell cell : cells)
            cell.tickBiology_ControlPhase();
    }

    private void updateCellEnvironments() {
        overlapDetection.findAndNotifyOverlaps();
        if (puller != null)
            puller.addForce();

        for (Cell cell1 : cells) {
            for (EnvironmentalInfluence influence1 : forceInfluences)
                influence1.updateEnvironment(cell1);

            for (PairBond bond : bonds)
                bond.addForces();
        }

        for (Cell cell : cells) {
            for (EnvironmentalInfluence influence : energyInfluences)
                influence.updateEnvironment(cell);
        }
    }

    private void updatePerLifecycleChanges() {
        cells.addAll(lifecycleListener.bornCells);
        overlapDetection.addCircles(lifecycleListener.bornCells);
        bonds.removeAll(lifecycleListener.brokenBonds);
        bonds.addAll(lifecycleListener.formedBonds);
    }

    public void restart() {
        cells.clear();
        overlapDetection.clear();
        bonds.clear();
    }

    public Collection<Cell> getCells() {
        return cells;
    }

    public Collection<PairBond> getBonds() {
        return bonds;
    }

    public void startPull(Cell cell) {
        puller = new Puller(cell);
    }

    public void setPullPoint(double x, double y) {
        puller.setPosition(x, y);
    }

    public void endPull() {
        puller = null;
    }

    public boolean isPulling() {
        return puller != null;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static int getSubticksPerTick() {
        return subticksPerTick;
    }

    public static void setSubticksPerTick(int val) {
        subticksPerTick = val;
    }
}
