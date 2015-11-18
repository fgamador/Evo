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
        cell.setPosition(0, 0);
    }

    @After
    public void tearDown() {
        PhotoRing.parameters.setTissueDensity(defaultTissueDensity);
    }

    @Test
    public void testGetArea() {
        Cell bigCell = new Cell(5);
        assertEquals(Math.PI * 25, bigCell.getArea(), 0);
    }

    @Test
    public void testGetMass() {
        assertEquals(PhotoRing.parameters.getTissueDensity() * Math.PI, new Cell(1).getMass(), 0);
    }

    @Test
    public void testMove_NoForces() {
        cell.move();

        assertVelocity(0, 0, cell);
        assertPosition(0, 0, cell);
    }

    @Test
    public void testMove_OneForce() {
        cell.addForce(0.5, -1);

        cell.move();

        assertVelocity(0.5, -1, cell);
        assertPosition(0.5, -1, cell);
    }

    @Test
    public void testMove_TwoForces() {
        cell.addForce(0.5, -1);
        cell.addForce(1.5, 2);

        cell.move();

        assertVelocity(2, 1, cell);
        assertPosition(2, 1, cell);
    }

    @Test
    public void testMove_Coasting() {
        cell.addForce(0.5, -1);
        cell.move();

        cell.move();

        assertVelocity(0.5, -1, cell);
        assertPosition(1, -2, cell);
    }

    @Test
    public void testMove_DoubleMass() {
        Cell heavyCell = new Cell(SQRT_2);
        heavyCell.addForce(1, -2);

        heavyCell.move();

        assertVelocity(0.5, -1, heavyCell);
        assertPosition(0.5, -1, heavyCell);
    }

    @Test
    public void testMove_SpeedLimit() {
        cell.setVelocity(8 / SQRT_2, -8 / SQRT_2);

        cell.move();

        assertEquals(4, CellPhysics.getSpeedLimit(), 0);
        assertVelocity(4 / SQRT_2, -4 / SQRT_2, cell);
    }

    @Test
    public void testCalcOverlapForce() {
        double defaultOverlapForceFactor = Cell.getOverlapForceFactor();
        try {
            Cell.setOverlapForceFactor(2);
            assertEquals(2, Cell.calcOverlapForce(1), 0);
        } finally {
            Cell.setOverlapForceFactor(defaultOverlapForceFactor);
        }
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        cell.setPosition(5, 0);

        assertEquals(0, cell.calcMinXWallCollisionForce(0), 0); // no contact
        assertEquals(0, cell.calcMinXWallCollisionForce(4), 0); // just touching
        assertEquals(0.5, cell.calcMinXWallCollisionForce(4.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        cell.setPosition(5, 0);

        assertEquals(0, cell.calcMaxXWallCollisionForce(10), 0); // no contact
        assertEquals(0, cell.calcMaxXWallCollisionForce(6), 0); // just touching
        assertEquals(-0.5, cell.calcMaxXWallCollisionForce(5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        cell.setPosition(0, -5);

        assertEquals(0, cell.calcMinYWallCollisionForce(-10), 0); // no contact
        assertEquals(0, cell.calcMinYWallCollisionForce(-6), 0); // just touching
        assertEquals(0.5, cell.calcMinYWallCollisionForce(-5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        cell.setPosition(0, -5);

        assertEquals(0, cell.calcMaxYWallCollisionForce(0), 0); // no contact
        assertEquals(0, cell.calcMaxYWallCollisionForce(-4), 0); // just touching
        assertEquals(-0.5, cell.calcMaxYWallCollisionForce(-4.5), 0); // overlap by 0.5
    }

    @Test
    public void testAddInterCellForces_XRestLength() {
        cell2.setPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_XCollision() {
        cell2.setPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_NotInCollision() {
        cell2.setPosition(3, -3);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_DiagonalCollision() {
        cell2.setPosition(1 / SQRT_2, -1 / SQRT_2);

        cell.addInterCellForces(cell2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_ReverseDiagonalCollision() {
        cell2.setPosition(1 / SQRT_2, -1 / SQRT_2);

        cell2.addInterCellForces(cell);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_FullOverlap() {
        cell2.setPosition(0, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingAtRest() {
        cell.addBond(cell2);
        cell2.setPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingMovingTogether() {
        cell.addBond(cell2);
        cell2.setPosition(2, 0);
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
//        cell2.setPosition(2, 0);
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
//        cell2.setPosition(2, 0);
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
        cell2.setPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_YTension() {
        cell.addBond(cell2);
        cell2.setPosition(0, -3);

        cell.addInterCellForces(cell2);

        assertNetForce(0, -1, cell);
        assertNetForce(0, 1, cell2);
    }
}
