package fga.evo.model;

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
        double totalEnergy = 100;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(1, donation));
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.tickBiology_ControlPhase();

        assertNotNull(child);
        assertEquals(child, cell.getChild());
        assertEquals(cell, child.getParent());
        assertBonded(cell, child);
        assertEquals(cell.getControl(), child.getControl());
        assertEquals(0, child.getPhotoArea(), 0);
        assertEquals(0, child.getRadius(), 0);
        assertEnergy(totalEnergy - donation, cell);
        assertCenterSeparation(cell.getRadius(), cell, child, 0);
        // TODO random angle

        // second tick
        child.addDonatedEnergy();
        Cell grandchild = child.tickBiology_ControlPhase();

        assertNull(grandchild);
        assertEquals(donation / PhotoRing.parameters.growthCost.getValue(), child.getPhotoArea(), 0.001);
        assertEquals(child.getPhotoRingOuterRadius(), child.getRadius(), 0);
    }

    @Test
    public void testControlPhase_ScaleChildDonation() {
        double donation = 10;
        Cell cell = new Cell(10, c -> {
            c.setSpawnOdds(1);
            c.requestChildDonation(donation);
            c.requestPhotoAreaResize(donation);
        });
        cell.addEnergy(10);
        double startPhotoArea = cell.getPhotoArea();

        Cell child = cell.tickBiology_ControlPhase();

        assertNotNull(child);
        assertEquals(donation / 2, child.getDonatedEnergy(), 0);
        assertEquals((donation / 2) / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea() - startPhotoArea, 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testControlPhase_MaintainChild() {
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(1, donation));
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology_ControlPhase();
        assertEquals(donation, child.getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, add-energy phase
        child.addDonatedEnergy();
        assertEquals(0, child.getDonatedEnergy(), 0);
        assertEnergy(donation, child);

        // second tick, use-energy phase
        cell.tickBiology_ControlPhase();

        assertEquals(child, cell.getChild());
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_ReleaseChild() {
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        control.setReleaseChildOdds(1);
        Cell cell = new Cell(10, control);
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology_ControlPhase();
        // second tick, add-energy phase
        child.addDonatedEnergy();
        // second tick, use-energy phase
        cell.tickBiology_ControlPhase();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_ReleaseParent() {
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        control.setReleaseParentOdds(1);
        Cell cell = new Cell(10, control);
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology_ControlPhase();
        // second tick, add-energy phase
        child.addDonatedEnergy();
        // second tick, use-energy phase
        cell.tickBiology_ControlPhase();
        child.tickBiology_ControlPhase();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testControlPhase_NegativeDonationDoesNothing() {
        double totalEnergy = 100;
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        Cell cell = new Cell(10, control);
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.tickBiology_ControlPhase();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);

        // second tick
        control.setDonation(-1);
        cell.tickBiology_ControlPhase();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);
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
    public void testDie() {
        Cell cell = new Cell(1);
        assertTrue(cell.isAlive());

        cell.die();

        assertFalse(cell.isAlive());
    }

    @Test
    public void testDecay() {
        Cell cell = new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setPhotoRingOuterRadius(2)
                .build();
        double initialFloatArea = cell.getFloatArea();
        double initialPhotoArea = cell.getPhotoArea();
        cell.die();

        cell.decay();

        double newFloatArea = initialFloatArea * (1 - FloatRing.parameters.decayRate.getValue());
        assertEquals(newFloatArea, cell.getFloatArea(), 0.001);
        double newFloatRadius = Math.sqrt(newFloatArea / Math.PI);
        assertEquals(newFloatRadius, cell.getFloatRingOuterRadius(), 0.001);
        double newPhotoArea = initialPhotoArea * (1 - PhotoRing.parameters.decayRate.getValue());
        assertEquals(newPhotoArea, cell.getPhotoArea(), 0.001);
        assertEquals(newFloatArea + newPhotoArea, cell.getArea(), 0.001);
    }
}
