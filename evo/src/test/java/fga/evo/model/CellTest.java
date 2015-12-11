package fga.evo.model;

import org.junit.Test;

import static fga.evo.model.Assert.*;
import static fga.evo.model.Util.sqr;
import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testGetMass() {
        assertEquals(PhotoRing.parameters.getTissueDensity() * Math.PI, new Cell(1).getMass(), 0);
    }

    @Test
    public void testGetArea() {
        Cell bigCell = new Cell(5);
        assertEquals(Math.PI * 25, bigCell.getArea(), 0);
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
        assertEnergy(-cell.getPhotoArea() * PhotoRing.parameters.getMaintenanceCost(), cell);
    }

    @Test
    public void testSubtractMaintenanceEnergy_PhotoAndFloatRings() {
        double totalEnergy = 100;
        double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(growthEnergy));
        cell.addEnergy(totalEnergy);
        cell.useEnergy();
        double remainingEnergy = cell.getEnergy();

        cell.subtractMaintenanceEnergy();

        double expectedMaintenanceEnergy = remainingEnergy
                - cell.getPhotoArea() * PhotoRing.parameters.getMaintenanceCost()
                - cell.getFloatArea() * FloatRing.parameters.getMaintenanceCost();
        assertEnergy(expectedMaintenanceEnergy, cell);
    }

    @Test
    public void testUseEnergy_FloatRingGrowth() {
        double totalEnergy = 100;
        double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(growthEnergy));
        assertEquals(0, cell.getFloatArea(), 0);
        cell.addEnergy(totalEnergy);

        cell.useEnergy();

        assertEquals(growthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(totalEnergy - growthEnergy, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth() {
        double totalEnergy = 100;
        double growthEnergy = 2;
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(growthEnergy));
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(totalEnergy);

        cell.useEnergy();

        assertEquals(Math.PI + growthEnergy / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEquals(totalEnergy - growthEnergy, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingShrinkage() {
        double radius = 2;
        double shrinkageEnergy = 0.1;
        Cell cell = new Cell(radius, c -> c.requestPhotoAreaResize(-shrinkageEnergy));
        double area = Math.PI * sqr(radius);
        assertEquals(area, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(area - shrinkageEnergy / PhotoRing.parameters.getShrinkageYield(), cell.getPhotoArea(), 0);
        assertEquals(shrinkageEnergy, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_PhotoRingGrowth_ExcessiveRequest() {
        Cell cell = new Cell(1, c -> c.requestPhotoAreaResize(1000)); // use all energy
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(2);

        cell.useEnergy();

        assertEquals(Math.PI + 2 / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEnergy(0, cell);
    }

    @Test
    public void testUseEnergy_FloatAndPhotoRingGrowth() {
        double totalEnergy = 100;
        double floatGrowthEnergy = 3;
        double photoGrowthEnergy = 2;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(photoGrowthEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(totalEnergy);

        cell.useEnergy();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(Math.PI + photoGrowthEnergy / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea(), 0);
        assertEquals(totalEnergy - (floatGrowthEnergy + photoGrowthEnergy), cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_OffsettingRequests() {
        double floatGrowthEnergy = 0.1;
        double photoShrinkageEnergy = 0.1;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(-photoShrinkageEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);

        cell.useEnergy();

        assertEquals(floatGrowthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0);
        assertEquals(Math.PI - photoShrinkageEnergy / PhotoRing.parameters.getShrinkageYield(), cell.getPhotoArea(), 0);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_ScaledOffsettingRequests() {
        double floatGrowthEnergy = 2;
        double photoShrinkageEnergy = 0.1;
        double totalEnergy = 1;
        Cell cell = new Cell(1, c -> {
            c.requestFloatAreaResize(floatGrowthEnergy);
            c.requestPhotoAreaResize(-photoShrinkageEnergy);
        });
        assertEquals(0, cell.getFloatArea(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0);
        cell.addEnergy(totalEnergy);

        cell.useEnergy();

        double scaledFloatGrowthEnergy = totalEnergy + photoShrinkageEnergy;
        assertEquals(scaledFloatGrowthEnergy / FloatRing.parameters.getGrowthCost(), cell.getFloatArea(), 0.00001);
        assertEquals(Math.PI - photoShrinkageEnergy / PhotoRing.parameters.getShrinkageYield(), cell.getPhotoArea(), 0);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_FloatRingGrowth2() {
        Cell cell = new Cell(1, c -> c.requestFloatAreaResize(1000)); // use all energy
        double oldPhotoRingArea = cell.getPhotoArea();
        cell.addEnergy(1);

        cell.useEnergy();

        assertTrue(cell.getFloatArea() > 0);
        assertEquals(oldPhotoRingArea, cell.getPhotoArea(), 0);
        assertEquals(cell.getFloatArea() + cell.getPhotoArea(), cell.getArea(), 0.00001);

        assertTrue(cell.getFloatRingOuterRadius() > 0);
        assertTrue(cell.getPhotoRingOuterRadius() > 1);
        assertTrue(cell.getPhotoRingOuterRadius() > cell.getFloatRingOuterRadius());
        assertEquals(cell.getPhotoRingOuterRadius(), cell.getRadius(), 0);

        assertEquals(cell.getFloatArea() * FloatRing.parameters.getTissueDensity()
                        + cell.getPhotoArea() * PhotoRing.parameters.getTissueDensity(),
                cell.getMass(), 0.00001);

        assertEnergy(0, cell);
    }

    @Test
    public void testUseEnergy_NoSpawnOddsNoChild() {
        double totalEnergy = 100;
        double spawnOdds = 0;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation));
        cell.addEnergy(totalEnergy);

        Cell child = cell.useEnergy();

        assertNull(child);
    }

    @Test
    public void testUseEnergy_SpawnOddsSuccessSpawnChild() {
        double totalEnergy = 100;
        double spawnOdds = 0.5;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation));
        cell.addEnergy(totalEnergy);
        Chance.setNextRandom(0.4);

        Cell child = cell.useEnergy();

        assertNotNull(child);
    }

    @Test
    public void testUseEnergy_NoDonatedEnergyNoChild() {
        double totalEnergy = 100;
        double spawnOdds = 1;
        double donation = 0;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation));
        cell.addEnergy(totalEnergy);

        Cell child = cell.useEnergy();

        assertNull(child);
    }

    @Test
    public void testUseEnergy_SpawnChild() {
        double totalEnergy = 100;
        double spawnOdds = 1;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation));
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.useEnergy();

        assertNotNull(child);
        assertEquals(child, cell.getChild());
        assertBonded(cell, child);
        assertEquals(cell.getControl(), child.getControl());
        assertEquals(0, child.getPhotoArea(), 0);
        assertEquals(0, child.getRadius(), 0);
        assertEnergy(totalEnergy - donation, cell);
        assertCenterSeparation(cell.getRadius(), cell, child, 0);
        // TODO random angle

        // second tick
        child.addDonatedEnergy();
        Cell grandchild = child.useEnergy();

        assertNull(grandchild);
        assertEquals(donation / PhotoRing.parameters.getGrowthCost(), child.getPhotoArea(), 0);
        assertEquals(child.getPhotoRingOuterRadius(), child.getRadius(), 0);
    }

    @Test
    public void testUseEnergy_ScaleChildDonation() {
        double totalEnergy = 10;
        double spawnOdds = 1;
        double donation = 10;
        Cell cell = new Cell(10, c -> {
            c.setSpawnOdds(spawnOdds);
            c.requestChildDonation(donation);
            c.requestPhotoAreaResize(donation);
        });
        cell.addEnergy(totalEnergy);
        double startPhotoArea = cell.getPhotoArea();

        Cell child = cell.useEnergy();

        assertNotNull(child);
        assertEquals(donation / 2, child.getDonatedEnergy(), 0);
        assertEquals((donation / 2) / PhotoRing.parameters.getGrowthCost(), cell.getPhotoArea() - startPhotoArea, 0.0001);
        assertEquals(0, cell.getEnergy(), 0);
    }

    @Test
    public void testUseEnergy_MaintainChild() {
        double totalEnergy = 100;
        double spawnOdds = 1;
        double donation = 2;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation));
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.useEnergy();
        assertEquals(donation, child.getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, add-energy phase
        child.addDonatedEnergy();
        assertEquals(0, child.getDonatedEnergy(), 0);
        assertEnergy(donation, child);

        // second tick, use-energy phase
        cell.useEnergy();

        assertEquals(child, cell.getChild());
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testUseEnergy_ReleaseChild() {
        double totalEnergy = 100;
        double spawnOdds = 1;
        double donation = 2;
        double releaseOdds = 1;
        Cell cell = new Cell(10, new ParentChildControl(spawnOdds, donation, releaseOdds));
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.useEnergy();
        assertEquals(donation, child.getDonatedEnergy(), 0);
        assertEnergy(0, child);

        // second tick, add-energy phase
        child.addDonatedEnergy();
        assertEquals(0, child.getDonatedEnergy(), 0);
        assertEnergy(donation, child);

        // second tick, use-energy phase
        cell.useEnergy();

        assertNull(cell.getChild());
        assertNotBonded(cell, child);
        assertEquals(donation, child.getDonatedEnergy(), 0);
    }

    @Test
    public void testUseEnergy_NegativeDonationDoesNothing() {
        double totalEnergy = 100;
        double spawnOdds = 1;
        double donation = 2;
        ParentChildControl control = new ParentChildControl(spawnOdds, donation);
        Cell cell = new Cell(10, control);
        cell.addEnergy(totalEnergy);

        // first tick
        Cell child = cell.useEnergy();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);

        // second tick
        control.setDonation(-1);
        cell.useEnergy();
        assertEquals(child, cell.getChild());
        assertEnergy(totalEnergy - donation, cell);
    }
}
