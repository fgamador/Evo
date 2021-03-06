package fga.evo.model;

import fga.evo.model.biology.AccumulatingCellLifecycleListener;
import fga.evo.model.biology.Cell;
import fga.evo.model.environment.EnvironmentalInfluence;
import fga.evo.model.environment.ForceInfluence;
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
    private static int movesPerTick = 2;

    private List<ForceInfluence> forceInfluences = new ArrayList<>();
    private List<EnvironmentalInfluence> environmentalInfluences = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();
    private List<PairBond> bonds = new ArrayList<>();
    private Puller puller;
    private AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
    private OverlapDetection overlapDetection = new OverlapDetection();

    public void addForceInfluence(ForceInfluence influence) {
        forceInfluences.add(influence);
    }

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
     */
    public Collection<Cell> tick() {
        lifecycleListener.clear();
        tickWithMonitoredLifecycles();
        updatePerLifecycleChanges();
        return lifecycleListener.bornCells;
    }

    private void tickWithMonitoredLifecycles() {
        detectOverlapsAndMoveCells();
        updateCellEnvironments();
        updateCells();
        runCellControls();
    }

    private void detectOverlapsAndMoveCells() {
        for (int i = 0; i < movesPerTick; i++) {
            for (Cell cell : cells)
                cell.getEnvironment().reset();

            detectOverlapsAndAddForcesToCells();

            for (Cell cell : cells)
                cell.move(movesPerTick);
        }
    }

    private void detectOverlapsAndAddForcesToCells() {
        overlapDetection.findAndNotifyOverlaps();

        for (Cell cell : cells) {
            for (ForceInfluence influence : forceInfluences)
                influence.addForce(cell);
        }

        for (PairBond bond : bonds)
            bond.addForces();

        if (puller != null)
            puller.addForce();
    }

    private void updateCellEnvironments() {
        for (Cell cell : cells) {
            for (EnvironmentalInfluence influence : environmentalInfluences)
                influence.updateEnvironment(cell);
        }
    }

    private void updateCells() {
        for (Cell cell : cells)
            cell.updateBiologyFromEnvironment();
    }

    private void runCellControls() {
        for (Cell cell : cells)
            cell.exertControl();
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

    public static int getMovesPerTick() {
        return movesPerTick;
    }

    public static void setMovesPerTick(int val) {
        movesPerTick = val;
    }
}
