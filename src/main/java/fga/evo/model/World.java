package fga.evo.model;

import fga.evo.model.geometry.OverlapDetection;
import fga.evo.model.physics.PairBond;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The world in which the cells live. The root container of the whole model. The entry point for simulation clock ticks.
 */
public class World {
    private static int subticksPerTick = 2;

    private List<EnvironmentalInfluence> environmentalInfluences = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<PairBond> bonds = new ArrayList<>();
    private Puller puller;
    private AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
    private OverlapDetection overlapDetection = new OverlapDetection();

    public void addEnvironmentalInfluence(EnvironmentalInfluence influence) {
        environmentalInfluences.add(influence);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
        overlapDetection.addCircle(cell);
        cell.setLifecycleListener(lifecycleListener);
    }

    /**
     * Propagates a simulation clock tick through the model.
     * TODO parallelize the loops
     */
    public Collection<Cell> tick() {
        lifecycleListener.clear();
        tickBiology();
        for (int i = 0; i < subticksPerTick; i++) {
            subtickPhysics();
        }
        cells.addAll(lifecycleListener.bornCells);
        overlapDetection.addCircles(lifecycleListener.bornCells);
        bonds.removeAll(lifecycleListener.brokenBonds);
        bonds.addAll(lifecycleListener.formedBonds);
        return lifecycleListener.bornCells;
    }

    private void tickBiology() {
        for (Cell cell : cells) {
            cell.tickBiology_ControlPhase();
        }

        for (Cell cell : cells) {
            tickBiology_ConsequencesPhase(cell);
        }
    }

    private void tickBiology_ConsequencesPhase(Cell cell) {
        for (EnvironmentalInfluence influence : environmentalInfluences) {
            influence.addEnergyToCell(cell);
        }
        cell.tickBiology_ConsequencesPhase();
    }

    private void subtickPhysics() {
        if (puller != null) {
            puller.addForce();
        }

        addForces();

        for (Cell cell : cells) {
            cell.subtickPhysics(subticksPerTick);
        }
    }

    private void addForces() {
        for (Cell cell : cells) {
            for (EnvironmentalInfluence influence : environmentalInfluences) {
                influence.addForcesToCell(cell);
            }

            for (PairBond bond : bonds) {
                bond.addForces();
            }
        }

        overlapDetection.findAndNotifyOverlaps();
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
