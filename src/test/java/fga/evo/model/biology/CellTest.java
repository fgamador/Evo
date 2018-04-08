package fga.evo.model.biology;

import fga.evo.model.control.ParentChildControl;
import fga.evo.model.physics.PairBond;
import fga.evo.model.util.Chance;
import org.junit.Test;

import static fga.evo.model.Assert.*;
import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void tickDecaysRecordedOverlap() {
        Cell cell1 = new Cell.Builder().build();
        Cell cell2 = new Cell.Builder().build();
        cell1.onOverlap(cell2, 0.5);

        cell1.updateBiologyFromEnvironment();
        assertApproxEquals(0.5, cell1.getRecentTotalOverlap());

        cell1.updateBiologyFromEnvironment();
        assertTrue(cell1.getRecentTotalOverlap() < 0.5);
    }

    @Test
    public void calculatesMassFromAreaAndDensity() {
        Cell subject = new Cell.Builder() //
                .withPhotoRingArea(Math.PI) //
                .build();
        assertApproxEquals(PhotoRing.parameters.density.getValue() * Math.PI, subject.getMass());
    }

    @Test
    public void calculatesNonFloatArea() {
        Cell subject = new Cell.Builder() //
                .withFloatRingArea(Math.PI) //
                .withPhotoRingArea(3 * Math.PI) //
                .build();
        assertApproxEquals(3 * Math.PI, subject.getNonFloatArea());
    }

    @Test
    public void testControlPhase_FloatRingGrowth() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestFloatAreaResize(2)) //
                .withEnergy(100) //
                .build();

        subject.exertControl();

        assertApproxEquals(2, subject.getFloatArea());
        assertEnergy(100 - 2 * FloatRing.parameters.growthCost.getValue(), subject);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestPhotoAreaResize(2)) //
                .withPhotoRingArea(Math.PI).withEnergy(100) //
                .build();

        subject.exertControl();

        assertApproxEquals(Math.PI + 2, subject.getPhotoArea());
        assertEnergy(100 - 2 * PhotoRing.parameters.growthCost.getValue(), subject);
    }

    @Test
    public void testControlPhase_PhotoRingShrinkage() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestPhotoAreaResize(-0.1)) //
                .withPhotoRingArea(Math.PI) //
                .build();

        subject.exertControl();

        assertApproxEquals(Math.PI - 0.1, subject.getPhotoArea());
        assertEnergy(0.1 * PhotoRing.parameters.shrinkageYield.getValue(), subject);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth_ExcessiveRequest() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestPhotoAreaResize(1000)) //
                .withPhotoRingArea(Math.PI).withEnergy(2) //
                .build();

        subject.exertControl();

        assertApproxEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), subject.getPhotoArea());
        assertEnergy(0, subject);
    }

    @Test
    public void testControlPhase_FloatAndPhotoRingGrowth() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> {
                    c.requestFloatAreaResize(3);
                    c.requestPhotoAreaResize(2);
                }) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(100) //
                .build();

        subject.exertControl();

        assertApproxEquals(3, subject.getFloatArea());
        assertApproxEquals(Math.PI + 2, subject.getPhotoArea());
        assertEnergy(100 - (3 * FloatRing.parameters.growthCost.getValue() + 2 * PhotoRing.parameters.growthCost.getValue()), subject);
    }

    @Test
    public void testControlPhase_OffsettingRequests() {
        FloatRing.parameters.growthCost.setValue(0.5);
        PhotoRing.parameters.shrinkageYield.setValue(0.5);
        Cell subject = new Cell.Builder() //
                .withControl(c -> {
                    c.requestFloatAreaResize(0.1);
                    c.requestPhotoAreaResize(-0.1);
                }) //
                .withPhotoRingArea(Math.PI) //
                .build();

        subject.exertControl();

        assertApproxEquals(0.1, subject.getFloatArea());
        assertApproxEquals(Math.PI - 0.1, subject.getPhotoArea());
        assertEnergy(0, subject);
    }

    @Test
    public void testControlPhase_ScaledOffsettingRequests() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> {
                    c.requestFloatAreaResize(2 / FloatRing.parameters.growthCost.getValue());
                    c.requestPhotoAreaResize(-0.1 / PhotoRing.parameters.shrinkageYield.getValue());
                }) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(1).build();

        subject.exertControl();

        double scaledFloatGrowthEnergy = 1 + 0.1;
        assertApproxEquals(scaledFloatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), subject.getFloatArea());
        assertApproxEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), subject.getPhotoArea());
        assertEnergy(0, subject);
    }

    @Test
    public void testControlPhase_FloatRingGrowthAffectsPhotoRingAndCell() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestFloatAreaResize(1000)) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(1) //
                .build();

        subject.exertControl();

        assertTrue(subject.getFloatArea() > 0);
        assertApproxEquals(Math.PI, subject.getPhotoArea());
        assertApproxEquals(subject.getFloatArea() + subject.getPhotoArea(), subject.getArea());

        assertTrue(subject.getFloatRingOuterRadius() > 0);
        assertTrue(subject.getPhotoRingOuterRadius() > 1);
        assertTrue(subject.getPhotoRingOuterRadius() > subject.getFloatRingOuterRadius());
        assertEquals(subject.getPhotoRingOuterRadius(), subject.getRadius(), 0);

        assertApproxEquals(subject.getFloatArea() * FloatRing.parameters.density.getValue() + subject.getPhotoArea() * PhotoRing.parameters.density.getValue(), subject.getMass());

        assertEnergy(0, subject);
    }

    @Test
    public void testControlPhase_NoSpawnOddsNoChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(0, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        subject.exertControl();

        assertTrue(lifecycleListener.bornCells.isEmpty());
        assertTrue(lifecycleListener.formedBonds.isEmpty());
    }

    @Test
    public void testControlPhase_SpawnOddsSuccessSpawnChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(0.5, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();
        Chance.setNextRandom(0.4);

        subject.exertControl();

        assertEquals(1, lifecycleListener.bornCells.size());
        assertEquals(1, lifecycleListener.formedBonds.size());
    }

    @Test
    public void testControlPhase_NoDonatedEnergyNoChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 0)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        subject.exertControl();

        assertTrue(lifecycleListener.bornCells.isEmpty());
        assertTrue(lifecycleListener.formedBonds.isEmpty());
    }

    @Test
    public void testControlPhase_SpawnChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        subject.exertControl();

        assertEquals(1, lifecycleListener.bornCells.size());
        Cell child = lifecycleListener.bornCells.get(0);
        assertNotNull(child);
        assertEquals(child, subject.getChild());
        assertEquals(subject, child.getParent());
        assertBonded(subject, child);
        assertEquals(subject.getControl(), child.getControl());
        assertEquals(subject.getLifecycleListener(), child.getLifecycleListener());
        assertEquals(Math.PI, child.getPhotoArea(), 0);
        assertEquals(1, child.getRadius(), 0);
        assertEnergy(100 - 2, subject);
        assertEnergy(2, child);
        assertCenterSeparation(subject.getRadius(), subject, child, 0);
    }

    @Test
    public void testControlPhase_ScaleChildDonation() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(c -> {
                    c.setSpawnOdds(1);
                    c.requestChildDonation(10);
                    c.requestPhotoAreaResize(10 / PhotoRing.parameters.growthCost.getValue());
                }) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(10) //
                .withLifecycleListener(lifecycleListener) //
                .build();
        double startPhotoArea = subject.getPhotoArea();

        subject.exertControl();

        Cell child = lifecycleListener.bornCells.get(0);
        assertEnergy(10 / 2, child);
        assertApproxEquals((10 / 2) / PhotoRing.parameters.growthCost.getValue(), subject.getPhotoArea() - startPhotoArea);
        assertEnergy(0, subject);
    }

    @Test
    public void testBothPhases_MaintainChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        // first tick, both phases
        subject.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        subject.updateBiologyFromEnvironment();
        assertEnergy(2, child);

        // second tick, first phase
        subject.exertControl();
        child.exertControl();
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, second phase
        subject.updateBiologyFromEnvironment();
        child.updateBiologyFromEnvironment();
        assertEquals(0, child.getEnvironment().getDonatedEnergy(), 0);
        assertEnergy(2 - child.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), child);
    }

    @Test
    public void testControlPhase_ReleaseChild() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2).setReleaseChildOdds(1)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        // first tick, both phases
        subject.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        PairBond bond = lifecycleListener.formedBonds.get(0);
        subject.updateBiologyFromEnvironment();

        // second tick, first phase
        subject.exertControl();
        child.exertControl();

        assertNull(subject.getChild());
        assertNull(child.getParent());
        assertNotBonded(subject, child);
        assertEquals(1, lifecycleListener.brokenBonds.size());
        assertEquals(bond, lifecycleListener.brokenBonds.get(0));
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_ReleaseParent() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withControl(new ParentChildControl(1, 2).setReleaseParentOdds(1)) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        // first tick, both phases
        subject.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        subject.updateBiologyFromEnvironment();

        // second tick, first phase
        subject.exertControl();
        child.exertControl();

        assertNull(subject.getChild());
        assertNull(child.getParent());
        assertNotBonded(subject, child);
        assertEquals(2, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_NegativeDonationDoesNothing() {
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        ParentChildControl control = new ParentChildControl(1, 2);
        Cell subject = new Cell.Builder() //
                .withControl(control) //
                .withPhotoRingOuterRadius(10) //
                .withEnergy(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        // first tick, both phases
        subject.exertControl();
        Cell child = lifecycleListener.bornCells.get(0);
        subject.updateBiologyFromEnvironment();

        // second tick, first phase
        control.setDonation(-1);
        subject.exertControl();
        assertEquals(child, subject.getChild());
        assertEquals(0, child.getEnvironment().getDonatedEnergy(), 0);
    }

    @Test
    public void testConsequencesPhase_PhotoRingMaintenanceEnergy() {
        Cell subject = new Cell.Builder() //
                .withPhotoRingOuterRadius(3) //
                .build();

        subject.updateBiologyFromEnvironment();

        assertEnergy(-subject.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), subject);
    }

    @Test
    public void testConsequencesPhase_PhotoAndFloatRingMaintenanceEnergy() {
        Cell subject = new Cell.Builder() //
                .withFloatRingOuterRadius(1) //
                .withPhotoRingOuterRadius(2) //
                .withEnergy(100) //
                .build();

        subject.updateBiologyFromEnvironment();

        double expectedEnergy = 100 - subject.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue() - subject.getFloatArea() * FloatRing.parameters.maintenanceCost.getValue();
        assertEnergy(expectedEnergy, subject);
    }

    @Test
    public void testConsequencesPhase_NoDamage() {
        Cell subject = new Cell.Builder() //
                .withPhotoRingOuterRadius(1) //
                .withEnergy(10) //
                .build();

        subject.updateBiologyFromEnvironment();

        assertEquals(0, subject.getDamage(), 0);
    }

    @Test
    public void testConsequencesPhase_Damage() {
        Cell subject = new Cell.Builder() //
                .withPhotoRingOuterRadius(1) //
                .build();

        subject.updateBiologyFromEnvironment();

        assertTrue(subject.isAlive());
        assertApproxEquals(PhotoRing.parameters.maintenanceCost.getValue() * subject.getPhotoArea(), subject.getDamage());
    }

    @Test
    public void testConsequencesPhase_DeadlyDamage() {
        Cell.maximumSurvivableDamage.setValue(0.1);
        AccumulatingCellLifecycleListener lifecycleListener = new AccumulatingCellLifecycleListener();
        Cell subject = new Cell.Builder() //
                .withPhotoRingArea(100) //
                .withLifecycleListener(lifecycleListener) //
                .build();

        subject.updateBiologyFromEnvironment();

        assertFalse(subject.isAlive());
        assertEquals(1, lifecycleListener.deadCells.size());
        assertEquals(subject, lifecycleListener.deadCells.get(0));
    }

    @Test
    public void testDie_Aliveness() {
        Cell subject = new Cell.Builder().build();
        assertTrue(subject.isAlive());

        subject.die();

        assertFalse(subject.isAlive());
    }

    @Test
    public void testControlPhase_Dead() {
        Cell subject = new Cell.Builder() //
                .withControl(c -> c.requestPhotoAreaResize(1)) //
                .withPhotoRingArea(Math.PI) //
                .withEnergy(10) //
                .build();
        subject.die();

        subject.exertControl();

        assertEquals(Math.PI, subject.getPhotoArea(), 0);
        assertEnergy(10, subject);
    }

    @Test
    public void testConsequencesPhase_Dead() {
        Cell subject = new Cell.Builder() //
                .withFloatRingOuterRadius(1) //
                .withPhotoRingOuterRadius(2) //
                .build();
        double initialFloatArea = subject.getFloatArea();
        double initialPhotoArea = subject.getPhotoArea();
        subject.die();

        subject.updateBiologyFromEnvironment();

        double newFloatArea = initialFloatArea * (1 - FloatRing.parameters.decayRate.getValue());
        assertApproxEquals(newFloatArea, subject.getFloatArea());
        double newFloatRadius = Math.sqrt(newFloatArea / Math.PI);
        assertApproxEquals(newFloatRadius, subject.getFloatRingOuterRadius());
        double newPhotoArea = initialPhotoArea * (1 - PhotoRing.parameters.decayRate.getValue());
        assertApproxEquals(newPhotoArea, subject.getPhotoArea());
        assertApproxEquals(newFloatArea + newPhotoArea, subject.getArea());
    }
}
