package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;

public class CellTest_Physics {
    public static final double SQRT_2 = Math.sqrt(2);

    private Cell cell, cell2;
    private double defaultTissueDensity;

    @Before
    public void setUp() {
        defaultTissueDensity = PhotoRing.parameters.getTissueDensity();
        // ensure that a cell with radius 1 has mass 1
        // TODO mock the mass
        PhotoRing.parameters.setTissueDensity(1 / Math.PI);
        cell = new Cell(1);
        cell2 = new Cell(1);
        cell.setCenterPosition(0, 0);
    }

    @After
    public void tearDown() {
        PhotoRing.parameters.setTissueDensity(defaultTissueDensity);
    }

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
    public void testAddInterCellForces_XRestLength() {
        cell2.setCenterPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_XCollision() {
        cell2.setCenterPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_NotInCollision() {
        cell2.setCenterPosition(3, -3);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_DiagonalCollision() {
        cell2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        cell.addInterCellForces(cell2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_ReverseDiagonalCollision() {
        cell2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        cell2.addInterCellForces(cell);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_FullOverlap() {
        cell2.setCenterPosition(0, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingAtRest() {
        cell.addBond(cell2);
        cell2.setCenterPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingMovingTogether() {
        cell.addBond(cell2);
        cell2.setCenterPosition(2, 0);
        cell.setVelocity(1, 0);
        cell2.setVelocity(1, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

//    @Test
//    public void testAddInterCellForces_Bonded_TouchingMovingIntoCompression() {
//        cell.setPhysics(2);
//        cell.addBond(cell2);
//        cell2.setCenterPosition(2, 0);
//        cell.setVelocity(1, 0);
//
//        cell.addInterCellForces(cell2);
//
//        assertNetForce(-0.5, 0, cell);
//        assertNetForce(0.5, 0, cell2);
//    }

//    @Test
//    public void testAddInterCellForces_Bonded_TouchingMovingIntoTension() {
//        cell.setPhysics(2);
//        cell.addBond(cell2);
//        cell2.setCenterPosition(2, 0);
//        cell2.setVelocity(1, 0);
//
//        cell.addInterCellForces(cell2);
//
//        assertNetForce(0.5, 0, cell);
//        assertNetForce(-0.5, 0, cell2);
//    }

    @Test
    public void testAddInterCellForces_Bonded_XCollision() {
        cell.addBond(cell2);
        cell2.setCenterPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_YTension() {
        cell.addBond(cell2);
        cell2.setCenterPosition(0, -3);

        cell.addInterCellForces(cell2);

        assertNetForce(0, -1, cell);
        assertNetForce(0, 1, cell2);
    }
}
