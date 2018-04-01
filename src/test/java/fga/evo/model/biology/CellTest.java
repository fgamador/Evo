package fga.evo.model.biology;

import fga.evo.model.control.ParentChildControl;
import fga.evo.model.physics.PairBond;
import fga.evo.model.util.Chance;
import org.junit.Assert;
import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testGetMass() {
        Cell cell = new Cell.Builder()
                .withPhotoRingArea(Math.PI)
                .build();
        Assert.assertEquals(PhotoRing.parameters.density.getValue() * Math.PI, cell.getMass(), 0.001);
    }

    @Test
    public void testGetNonFloatArea() {
        Cell cell = new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .withPhotoRingArea(3 * Math.PI)
                .build();
        assertEquals(3 * Math.PI, cell.getNonFloatArea(), 0.001);
    }

    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell.Builder()
                .withPhotoRingOuterRadius(3)
                .build();

        cell.getEnvironment().setLightIntensity(2);
        cell.photosynthesize();

        assertEnergy(4.5, cell);
    }

    @Test
    public void testControlPhase_FloatRingGrowth() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestFloatAreaResize(2))
                .withEnergy(100)
                .build();

        cell.exertControl();

        Assert.assertEquals(2 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(100 - 2, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestPhotoAreaResize(2))
                .withPhotoRingArea(Math.PI)
                .withEnergy(100)
                .build();

        cell.exertControl();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(100 - 2, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingShrinkage() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestPhotoAreaResize(-0.1))
                .withPhotoRingArea(Math.PI)
                .build();

        cell.exertControl();

        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0.1, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestPhotoAreaResize(1000))
                .withPhotoRingArea(Math.PI)
                .withEnergy(2)
                .build();

        cell.exertControl();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEnergy(0, cell);
    }

    @Test
    public void testControlPhase_FloatAndPhotoRingGrowth() {
        Cell cell = new Cell.Builder()
                .withControl(c -> {
                    c.requestFloatAreaResize(3);
                    c.requestPhotoAreaResize(2);
                })
                .withPhotoRingArea(Math.PI)
                .withEnergy(100)
                .build();

        cell.exertControl();

        assertEquals(3 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(100 - (3 + 2), cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_OffsettingRequests() {
        Cell cell = new Cell.Builder()
                .withControl(c -> {
                    c.requestFloatAreaResize(0.1);
                    c.requestPhotoAreaResize(-0.1);
                })
                .withPhotoRingArea(Math.PI)
                .build();

        cell.exertControl();

        assertEquals(0.1 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testControlPhase_ScaledOffsettingRequests() {
        Cell cell = new Cell.Builder()
                .withControl(c -> {
                    c.requestFloatAreaResize(2);
                    c.requestPhotoAreaResize(-0.1);
                })
                .withPhotoRingArea(Math.PI)
                .withEnergy(1)
                .build();

        cell.exertControl();

        double scaledFloatGrowthEnergy = 1 + 0.1;
        assertEquals(scaledFloatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testControlPhase_FloatRingGrowthAffectsPhotoRingAndCell() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestFloatAreaResize(1000))
                .withPhotoRingArea(Math.PI)
                .withEnergy(1)
                .build();

        cell.exertControl();

        assertTrue(cell.getFloatArea() > 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        assertEquals(cell.getFloatArea() + cell.getPhotoArea(), cell.getArea(), 0.001);

        assertTrue(cell.getFloatRingOuterRadius() > 0);
        assertTrue(cell.getPhotoRingOuterRadius() > 1);
        assertTrue(cell.getPhotoRingOuterRadius() > cell.getFloatRingOuterRadius());
        assertEquals(cell.getPhotoRingOuterRadius(), cell.getRadius(), 0);

        assertEquals(cell.getFloatArea() * FloatRing.parameters.density.getValue()
                        + cell.getPhotoArea() * PhotoRing.parameters.density.getValue(),
                cell.getMass(), 0.001);

        assertEnergy(0, cell);
    }

    @Test
    public void testControlPhase_NoSpawnOddsNoChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(0, 2))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        cell.exertControl();

        assertTrue(lifecycleListener.bornCells.isEmpty());
        assertTrue(lifecycleListener.formedBonds.isEmpty());
    }

    @Test
    public void testControlPhase_SpawnOddsSuccessSpawnChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(0.5, 2))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();
        Chance.setNextRandom(0.4);

        cell.exertControl();

        assertEquals(1, lifecycleListener.bornCells.size());
        assertEquals(1, lifecycleListener.formedBonds.size());
    }

    @Test
    public void testControlPhase_NoDonatedEnergyNoChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(1, 0))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        cell.exertControl();

        assertTrue(lifecycleListener.bornCells.isEmpty());
        assertTrue(lifecycleListener.formedBonds.isEmpty());
    }

    @Test
    public void testControlPhase_SpawnChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(1, 2))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        cell.exertControl();

        assertEquals(1, lifecycleListener.bornCells.size());
        Cell child = lifecycleListener.bornCells.get(0);
        assertNotNull(child);
        assertEquals(child, cell.getChild());
        assertEquals(cell, child.getParent());
        assertBonded(cell, child);
        assertEquals(cell.getControl(), child.getControl());
        assertEquals(cell.getLifecycleListener(), child.getLifecycleListener());
        assertEquals(Math.PI, child.getPhotoArea(), 0);
        assertEquals(1, child.getRadius(), 0);
        assertEnergy(100 - 2, cell);
        assertEnergy(2, child);
        assertCenterSeparation(cell.getRadius(), cell, child, 0);
    }

    @Test
    public void testControlPhase_ScaleChildDonation() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(c -> {
                    c.setSpawnOdds(1);
                    c.requestChildDonation(10);
                    c.requestPhotoAreaResize(10);
                })
                .withPhotoRingOuterRadius(10)
                .withEnergy(10)
                .withLifecycleListener(lifecycleListener)
                .build();
        double startPhotoArea = cell.getPhotoArea();

        cell.exertControl();

        Cell child = lifecycleListener.bornCells.get(0);
        assertEnergy(10 / 2, child);
        assertEquals((10 / 2) / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea() - startPhotoArea, 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testBothPhases_MaintainChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(1, 2))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        // first tick, both phases
        cell.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        cell.updateBiologyFromEnvironment();
        assertEnergy(2, child);

        // second tick, first phase
        cell.exertControl();
        child.exertControl();
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, second phase
        cell.updateBiologyFromEnvironment();
        child.updateBiologyFromEnvironment();
        assertEquals(0, child.getEnvironment().getDonatedEnergy(), 0);
        assertEnergy(2 - child.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), child);
    }

    @Test
    public void testControlPhase_ReleaseChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(1, 2).setReleaseChildOdds(1))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        // first tick, both phases
        cell.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        PairBond bond = lifecycleListener.formedBonds.get(0);
        cell.updateBiologyFromEnvironment();

        // second tick, first phase
        cell.exertControl();
        child.exertControl();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(1, lifecycleListener.brokenBonds.size());
        assertEquals(bond, lifecycleListener.brokenBonds.get(0));
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_ReleaseParent() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withControl(new ParentChildControl(1, 2).setReleaseParentOdds(1))
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        // first tick, both phases
        cell.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        cell.updateBiologyFromEnvironment();

        // second tick, first phase
        cell.exertControl();
        child.exertControl();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_NegativeDonationDoesNothing() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        ParentChildControl control = new ParentChildControl(1, 2);
        Cell cell = new Cell.Builder()
                .withControl(control)
                .withPhotoRingOuterRadius(10)
                .withEnergy(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        // first tick, both phases
        cell.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        cell.updateBiologyFromEnvironment();

        // second tick, first phase
        control.setDonation(-1);
        cell.exertControl();
        assertEquals(child, cell.getChild());
        assertEquals(0, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testConsequencesPhase_PhotoRingMaintenanceEnergy() {
        Cell cell = new Cell.Builder()
                .withPhotoRingOuterRadius(3)
                .build();

        cell.updateBiologyFromEnvironment();

        assertEnergy(-cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), cell);
    }

    @Test
    public void testConsequencesPhase_PhotoAndFloatRingMaintenanceEnergy() {
        Cell cell = new Cell.Builder()
                .withFloatRingOuterRadius(1)
                .withPhotoRingOuterRadius(2)
                .withEnergy(100)
                .build();

        cell.updateBiologyFromEnvironment();

        double expectedEnergy = 100
                - cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue()
                - cell.getFloatArea() * FloatRing.parameters.maintenanceCost.getValue();
        assertEnergy(expectedEnergy, cell);
    }

    @Test
    public void testConsequencesPhase_NoDamage() {
        Cell cell = new Cell.Builder()
                .withPhotoRingOuterRadius(1)
                .withEnergy(10)
                .build();

        cell.updateBiologyFromEnvironment();

        assertEquals(0, cell.getDamage(), 0);
    }

    @Test
    public void testConsequencesPhase_Damage() {
        Cell cell = new Cell.Builder()
                .withPhotoRingOuterRadius(1)
                .build();

        cell.updateBiologyFromEnvironment();

        assertTrue(cell.isAlive());
        assertEquals(PhotoRing.parameters.maintenanceCost.getValue() * cell.getPhotoArea(),
                cell.getDamage(), 0.001);
    }

    @Test
    public void testConsequencesPhase_DeadlyDamage() {
        Cell.maximumSurvivableDamage.setValue(0.1);
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell cell = new Cell.Builder()
                .withPhotoRingArea(100)
                .withLifecycleListener(lifecycleListener)
                .build();

        cell.updateBiologyFromEnvironment();

        assertFalse(cell.isAlive());
        assertEquals(1, lifecycleListener.deadCells.size());
        assertEquals(cell, lifecycleListener.deadCells.get(0));
    }

    @Test
    public void testDie_Aliveness() {
        Cell cell = new Cell.Builder().build();
        assertTrue(cell.isAlive());

        cell.die();

        assertFalse(cell.isAlive());
    }

    @Test
    public void testControlPhase_Dead() {
        Cell cell = new Cell.Builder()
                .withControl(c -> c.requestPhotoAreaResize(1))
                .withPhotoRingArea(Math.PI)
                .withEnergy(10)
                .build();
        cell.die();

        cell.exertControl();

        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        assertEnergy(10, cell);
    }

    @Test
    public void testConsequencesPhase_Dead() {
        Cell cell = new Cell.Builder()
                .withFloatRingOuterRadius(1)
                .withPhotoRingOuterRadius(2)
                .build();
        double initialFloatArea = cell.getFloatArea();
        double initialPhotoArea = cell.getPhotoArea();
        cell.die();

        cell.updateBiologyFromEnvironment();

        double newFloatArea = initialFloatArea * (1 - FloatRing.parameters.decayRate.getValue());
        assertEquals(newFloatArea, cell.getFloatArea(), 0.001);
        double newFloatRadius = Math.sqrt(newFloatArea / Math.PI);
        assertEquals(newFloatRadius, cell.getFloatRingOuterRadius(), 0.001);
        double newPhotoArea = initialPhotoArea * (1 - PhotoRing.parameters.decayRate.getValue());
        assertEquals(newPhotoArea, cell.getPhotoArea(), 0.001);
        assertEquals(newFloatArea + newPhotoArea, cell.getArea(), 0.001);
    }
}
