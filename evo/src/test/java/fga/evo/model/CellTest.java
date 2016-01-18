package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static fga.evo.model.Util.sqr;
import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testGetMass() {
        Cell cell = new Cell(1);
        assertEquals(PhotoRing.parameters.density.getValue() * Math.PI, cell.getMass(), 0.001);
    }

    @Test
    public void testGetArea() {
        Cell bigCell = new Cell(5);
        assertEquals(25 * Math.PI, bigCell.getArea(), 0.001);
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
        Cell cell = new Cell(3);
        cell.photosynthesize(2);
        assertEnergy(4.5, cell);
    }

    @Test
    public void testSubtractMaintenanceEnergy_PhotoRing() {
        Cell cell = new Cell(3);
        cell.subtractMaintenanceEnergy();
        assertEnergy(-cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue(), cell);
    }

    @Test
    public void testSubtractMaintenanceEnergy_PhotoAndFloatRings() {
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(2));
        cell.addEnergy(100);
        cell.tickBiology();
        double remainingEnergy = cell.getEnergy();

        cell.subtractMaintenanceEnergy();

        double expectedEnergy = remainingEnergy
                - cell.getPhotoArea() * PhotoRing.parameters.maintenanceCost.getValue()
                - cell.getFloatArea() * FloatRing.parameters.maintenanceCost.getValue();
        assertEnergy(expectedEnergy, cell);
    }

    @Test
    public void testUseEnergy_FloatRingGrowth() {
        double totalEnergy = 100;
        double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(growthEnergy));
        assertEquals(0, cell.getFloatArea(), 0);
        cell.addEnergy(totalEnergy);

        cell.tickBiology();

        assertEquals(growthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(totalEnergy - growthEnergy, cell.getEnergy(), 0.001);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        double totalEnergy = 100;
        double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(growthEnergy));
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);
        cell.addEnergy(totalEnergy);

        cell.tickBiology();

        assertEquals(Math.PI + growthEnergy / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(totalEnergy - growthEnergy, cell.getEnergy(), 0.001);
    }

    @Test
    public void testUseEnergy_PhotoRingShrinkage() {
        double radius = 2;
        double shrinkageEnergy = 0.1;
        Cell cell = new Cell(radius, c -> c.requestPhotoAreaResize(-shrinkageEnergy));
        double area = Math.PI * sqr(radius);
        assertEquals(area, cell.getPhotoArea(), 0.001);

        cell.tickBiology();

        assertEquals(area - shrinkageEnergy / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(shrinkageEnergy, cell.getEnergy(), 0.001);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(1000)); // use all energy
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);
        cell.addEnergy(2);

        cell.tickBiology();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEnergy(0, cell);
    }

    @Test
    public void testTickBiology_FloatAndPhotoRingGrowth() {
        double totalEnergy = 100;
        double floatGrowthEnergy = 3;
        double photoGrowthEnergy = 2;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(photoGrowthEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);
        cell.addEnergy(totalEnergy);

        cell.tickBiology();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI + photoGrowthEnergy / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(totalEnergy - (floatGrowthEnergy + photoGrowthEnergy), cell.getEnergy(), 0.001);
    }

    @Test
    public void testTickBiology_OffsettingRequests() {
        double floatGrowthEnergy = 0.1;
        double photoShrinkageEnergy = 0.1;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(-photoShrinkageEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);

        cell.tickBiology();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - photoShrinkageEnergy / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testTickBiology_ScaledOffsettingRequests() {
        double floatGrowthEnergy = 2;
        double photoShrinkageEnergy = 0.1;
        double totalEnergy = 1;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(-photoShrinkageEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);
        cell.addEnergy(totalEnergy);

        cell.tickBiology();

        double scaledFloatGrowthEnergy = totalEnergy + photoShrinkageEnergy;
        assertEquals(scaledFloatGrowthEnergy / FloatRing.parameters.growthCost.getValue(), cell.getFloatArea(), 0.001);
        assertEquals(Math.PI - photoShrinkageEnergy / PhotoRing.parameters.shrinkageYield.getValue(), cell.getPhotoArea(), 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testTickBiology_FloatRingGrowth2() {
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(1000)); // use all energy
        double oldPhotoRingArea = cell.getPhotoArea();
        cell.addEnergy(1);

        cell.tickBiology();

        assertTrue(cell.getFloatArea() > 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoArea(), 0);
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
    public void testTickBiology_NoSpawnOddsNoChild() {
        Cell cell = new Cell(10, new ParentChildControl(0, 2));
        cell.addEnergy(100);

        Cell child = cell.tickBiology();

        assertNull(child);
    }

    @Test
    public void testTickBiology_SpawnOddsSuccessSpawnChild() {
        Cell cell = new Cell(10, new ParentChildControl(0.5, 2));
        cell.addEnergy(100);
        Chance.setNextRandom(0.4);

        Cell child = cell.tickBiology();

        assertNotNull(child);
    }

    @Test
    public void testTickBiology_NoDonatedEnergyNoChild() {
        Cell cell = new Cell(10, new ParentChildControl(1, 0));
        cell.addEnergy(100);

        Cell child = cell.tickBiology();

        assertNull(child);
    }

    @Test
    public void testTickBiology_SpawnChild() {
        double totalEnergy = 100;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(1, donation));
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.tickBiology();

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
        Cell grandchild = child.tickBiology();

        assertNull(grandchild);
        assertEquals(donation / PhotoRing.parameters.growthCost.getValue(), child.getPhotoArea(), 0.001);
        assertEquals(child.getPhotoRingOuterRadius(), child.getRadius(), 0);
    }

    @Test
    public void testTickBiology_ScaleChildDonation() {
        double donation = 10;
        Cell cell = new Cell(10, c -> {
            c.setSpawnOdds(1);
            c.requestChildDonation(donation);
            c.requestPhotoAreaResize(donation);
        });
        cell.addEnergy(10);
        double startPhotoArea = cell.getPhotoArea();

        Cell child = cell.tickBiology();

        assertNotNull(child);
        assertEquals(donation / 2, child.getDonatedEnergy(), 0);
        assertEquals((donation / 2) / PhotoRing.parameters.growthCost.getValue(), cell.getPhotoArea() - startPhotoArea, 0.001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testTickBiology_MaintainChild() {
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(1, donation));
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology();
        assertEquals(donation, child.getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, add-energy phase
        child.addDonatedEnergy();
        assertEquals(0, child.getDonatedEnergy(), 0);
        assertEnergy(donation, child);

        // second tick, use-energy phase
        cell.tickBiology();

        assertEquals(child, cell.getChild());
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testTickBiology_ReleaseChild() {
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        control.setReleaseChildOdds(1);
        Cell cell = new Cell(10, control);
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology();
        // second tick, add-energy phase
        child.addDonatedEnergy();
        // second tick, use-energy phase
        cell.tickBiology();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testTickBiology_ReleaseParent() {
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        control.setReleaseParentOdds(1);
        Cell cell = new Cell(10, control);
        cell.addEnergy(100);

        // first tick
        Cell child = cell.tickBiology();
        // second tick, add-energy phase
        child.addDonatedEnergy();
        // second tick, use-energy phase
        cell.tickBiology();
        child.tickBiology();

        assertNull(cell.getChild());
        assertNull(child.getParent());
        assertNotBonded(cell, child);
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testTickBiology_NegativeDonationDoesNothing() {
        double totalEnergy = 100;
        double donation = 2;
        ParentChildControl control = new ParentChildControl(1, donation);
        Cell cell = new Cell(10, control);
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.tickBiology();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);

        // second tick
        control.setDonation(-1);
        cell.tickBiology();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);
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
