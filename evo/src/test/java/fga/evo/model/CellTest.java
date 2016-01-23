package fga.evo.model;

import org.jcp.xml.dsig.internal.MacOutputStream;
import org.junit.Test;

import static fga.evo.model.Assert.*;
import static fga.evo.model.Util.sqr;
import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testGetMass() {
        Cell cell = new Cell.Builder()
                .setPhotoRingArea(Math.PI)
                .build();
        assertEquals(PhotoRing.parameters.density.getValue() * Math.PI, cell.getMass(), 0.001);
    }

    @Test
    public void testGetNonFloatArea() {
        Cell cell = new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .setPhotoRingArea(3 * Math.PI)
                .build();
        assertEquals(3 * Math.PI, cell.getNonFloatArea(), 0.001);
    }

    @Test
    public void testPhotosynthesize() {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(3)
                .build();

        cell.photosynthesize(2);

        assertEnergy(4.5, cell);
    }

    @Test
    public void testControlPhase_FloatRingGrowth() {
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestFloatAreaResize(2))
                .setEnergy(100)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(2 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(100 - 2, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth() {
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestPhotoAreaResize(2))
                .setPhotoRingArea(Math.PI)
                .setEnergy(100)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(100 - 2, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingShrinkage() {
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestPhotoAreaResize(-0.1))
                .setPhotoRingArea(Math.PI)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0.1, cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestPhotoAreaResize(1000))
                .setPhotoRingArea(Math.PI)
                .setEnergy(2)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEnergy(0, cell);
    }

    @Test
    public void testControlPhase_FloatAndPhotoRingGrowth() {
        Cell cell = new Cell.Builder()
                .setControl(c -> {
                    c.requestFloatAreaResize(3);
                    c.requestPhotoAreaResize(2);
                })
                .setPhotoRingArea(Math.PI)
                .setEnergy(100)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(3 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(100 - (3 + 2), cell.getEnergy(), 0.001);
    }

    @Test
    public void testControlPhase_OffsettingRequests() {
        Cell cell = new Cell.Builder()
                .setControl(c -> {
                    c.requestFloatAreaResize(0.1);
                    c.requestPhotoAreaResize(-0.1);
                })
                .setPhotoRingArea(Math.PI)
                .build();

        cell.tickBiology_ControlPhase();

        assertEquals(0.1 / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testControlPhase_ScaledOffsettingRequests() {
        Cell cell = new Cell.Builder()
                .setControl(c -> {
                    c.requestFloatAreaResize(2);
                    c.requestPhotoAreaResize(-0.1);
                })
                .setPhotoRingArea(Math.PI)
                .setEnergy(1)
                .build();

        cell.tickBiology_ControlPhase();

        double scaledFloatGrowthEnergy = 1 + 0.1;
        assertEquals(scaledFloatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - 0.1 / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testControlPhase_FloatRingGrowthAffectsPhotoRingAndCell() {
        Cell cell = new Cell.Builder()
                .setControl(c -> c.requestFloatAreaResize(1000))
                .setPhotoRingArea(Math.PI)
                .setEnergy(1)
                .build();

        cell.tickBiology_ControlPhase();

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
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(0, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        Cell child = cell.tickBiology_ControlPhase();

        assertNull(child);
    }

    @Test
    public void testControlPhase_SpawnOddsSuccessSpawnChild() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(0.5, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();
        Chance.setNextRandom(0.4);

        Cell child = cell.tickBiology_ControlPhase();

        assertNotNull(child);
    }

    @Test
    public void testControlPhase_NoDonatedEnergyNoChild() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 0))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        Cell child = cell.tickBiology_ControlPhase();

        assertNull(child);
    }

    @Test
    public void testControlPhase_SpawnChild() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        Cell child = cell.tickBiology_ControlPhase();

        assertNotNull(child);
        assertEquals(child, cell.getChild());
        assertEquals(cell, child.getParent());
        assertBonded(cell, child);
        assertEquals(cell.getControl(), child.getControl());
        assertEquals(0, child.getPhotoArea(), 0);
        assertEquals(0, child.getRadius(), 0);
        assertEnergy(100 - 2, cell);
        assertEnergy(2, child);
        assertCenterSeparation(cell.getRadius(), cell, child, 0);
    }

    @Test
    public void testControlPhase_ScaleChildDonation() {
        Cell cell = new Cell.Builder()
                .setControl(c -> {
                    c.setSpawnOdds(1);
                    c.requestChildDonation(10);
                    c.requestPhotoAreaResize(10);
                })
                .setPhotoRingOuterRadius(10)
                .setEnergy(10)
                .build();
        double startPhotoArea = cell.getPhotoArea();

        Cell child = cell.tickBiology_ControlPhase();

        assertNotNull(child);
        assertEnergy(10 / 2, child);
        assertEquals((10 / 2) / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea() - startPhotoArea, 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testBothPhases_MaintainChild() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        // first tick, both phases
        Cell child = cell.tickBiology_ControlPhase();
        cell.tickBiology_ConsequencesPhase();
        assertEnergy(2, child);

        // second tick, first phase
        cell.tickBiology_ControlPhase();
        child.tickBiology_ControlPhase();
        assertEquals(2, child.getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, second phase
        cell.tickBiology_ConsequencesPhase();
        child.tickBiology_ConsequencesPhase();
        assertEquals(0, child.getDonatedEnergy(), 0);
        assertEnergy(2 - child.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), child);
    }

    @Test
    public void testControlPhase_ReleaseChild() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2).setReleaseChildOdds(1))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        // first tick, both phases
        Cell child = cell.tickBiology_ControlPhase();
        cell.tickBiology_ConsequencesPhase();

        // second tick, first phase
        cell.tickBiology_ControlPhase();
        child.tickBiology_ControlPhase();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(2, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_ReleaseParent() {
        Cell cell = new Cell.Builder()
                .setControl(new ParentChildControl(1, 2).setReleaseParentOdds(1))
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        // first tick, both phases
        Cell child = cell.tickBiology_ControlPhase();
        cell.tickBiology_ConsequencesPhase();

        // second tick, first phase
        cell.tickBiology_ControlPhase();
        child.tickBiology_ControlPhase();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(2, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_NegativeDonationDoesNothing() {
        ParentChildControl control = new ParentChildControl(1, 2);
        Cell cell = new Cell.Builder()
                .setControl(control)
                .setPhotoRingOuterRadius(10)
                .setEnergy(100)
                .build();

        // first tick, both phases
        Cell child = cell.tickBiology_ControlPhase();
        cell.tickBiology_ConsequencesPhase();

        // second tick, first phase
        control.setDonation(-1);
        cell.tickBiology_ControlPhase();
        assertEquals(child, cell.getChild());
        assertEquals(0, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testConsequencesPhase_PhotoRingMaintenanceEnergy() {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(3)
                .build();

        cell.tickBiology_ConsequencesPhase();

        assertEnergy(-cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), cell);
    }

    @Test
    public void testConsequencesPhase_PhotoAndFloatRingMaintenanceEnergy() {
        Cell cell = new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setPhotoRingOuterRadius(2)
                .setEnergy(100)
                .build();

        cell.tickBiology_ConsequencesPhase();

        double expectedEnergy = 100
                - cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue()
                - cell.getFloatArea() * FloatRing.parameters.maintenanceCost.getValue();
        assertEnergy(expectedEnergy, cell);
    }

    @Test
    public void testConsequencesPhase_NoDamage() {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(1)
                .setEnergy(10)
                .build();

        cell.tickBiology_ConsequencesPhase();

        assertEquals(0, cell.getDamage(), 0);
    }

    @Test
    public void testConsequencesPhase_Damage() {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(1)
                .build();

        cell.tickBiology_ConsequencesPhase();

        assertEquals(-PhotoRing.parameters.maintenanceCost.getValue() * cell.getPhotoArea(),
                cell.getDamage(), 0.001);
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
                .setControl(c -> c.requestPhotoAreaResize(1))
                .setPhotoRingArea(Math.PI)
                .setEnergy(10)
                .build();
        cell.die();

        cell.tickBiology_ControlPhase();

        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        assertEnergy(10, cell);
    }

    @Test
    public void testConsequencesPhase_Dead() {
        Cell cell = new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setPhotoRingOuterRadius(2)
                .build();
        double initialFloatArea = cell.getFloatArea();
        double initialPhotoArea = cell.getPhotoArea();
        cell.die();

        cell.tickBiology_ConsequencesPhase();

        double newFloatArea = initialFloatArea * (1 - FloatRing.parameters.decayRate.getValue());
        assertEquals(newFloatArea, cell.getFloatArea(), 0.001);
        double newFloatRadius = Math.sqrt(newFloatArea / Math.PI);
        assertEquals(newFloatRadius, cell.getFloatRingOuterRadius(), 0.001);
        double newPhotoArea = initialPhotoArea * (1 - PhotoRing.parameters.decayRate.getValue());
        assertEquals(newPhotoArea, cell.getPhotoArea(), 0.001);
        assertEquals(newFloatArea + newPhotoArea, cell.getArea(), 0.001);
    }
}
