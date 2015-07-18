package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;
import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;

public class CellTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private Cell cell, cell2;

    @Before
    public void setUp() {
        cell = new Cell(1, 1);
        cell2 = new Cell(1, 1);
        cell.setPosition(0, 0);
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
        Cell heavyCell = new Cell(2, 1);
        heavyCell.addForce(1, -2);

        heavyCell.move();

        assertVelocity(0.5, -1, heavyCell);
        assertPosition(0.5, -1, heavyCell);
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

        assertEquals(0, cell.calcLowXWallCollisionForce(0), 0); // no contact
        assertEquals(0, cell.calcLowXWallCollisionForce(4), 0); // just touching
        assertEquals(0.5, cell.calcLowXWallCollisionForce(4.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        cell.setPosition(5, 0);

        assertEquals(0, cell.calcHighXWallCollisionForce(10), 0); // no contact
        assertEquals(0, cell.calcHighXWallCollisionForce(6), 0); // just touching
        assertEquals(-0.5, cell.calcHighXWallCollisionForce(5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        cell.setPosition(0, 5);

        assertEquals(0, cell.calcLowYWallCollisionForce(0), 0); // no contact
        assertEquals(0, cell.calcLowYWallCollisionForce(4), 0); // just touching
        assertEquals(0.5, cell.calcLowYWallCollisionForce(4.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        cell.setPosition(0, 5);

        assertEquals(0, cell.calcHighYWallCollisionForce(10), 0); // no contact
        assertEquals(0, cell.calcHighYWallCollisionForce(6), 0); // just touching
        assertEquals(-0.5, cell.calcHighYWallCollisionForce(5.5), 0); // overlap by 0.5
    }

    @Test
    public void testAddInterCellForces_XRestLength() {
        cell2.setPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertForce(0, 0, cell);
        assertForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_XCollision() {
        cell2.setPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertForce(-1, 0, cell);
        assertForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_NotInCollision() {
        cell2.setPosition(3, 3);

        cell.addInterCellForces(cell2);

        assertForce(0, 0, cell);
        assertForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_DiagonalCollision() {
        cell2.setPosition(1 / SQRT_2, 1 / SQRT_2);

        cell.addInterCellForces(cell2);

        assertForce(-SQRT_2 / 2, -SQRT_2 / 2, cell);
        assertForce(SQRT_2 / 2, SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_ReverseDiagonalCollision() {
        cell2.setPosition(1 / SQRT_2, 1 / SQRT_2);

        cell2.addInterCellForces(cell);

        assertForce(-SQRT_2 / 2, -SQRT_2 / 2, cell);
        assertForce(SQRT_2 / 2, SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_FullOverlap() {
        cell2.setPosition(0, 0);

        cell.addInterCellForces(cell2);

        assertForce(0, 0, cell);
        assertForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_XRestLength() {
        cell.addBond(cell2);
        cell2.setPosition(2, 0);

        cell.addInterCellForces(cell2);

        assertForce(0, 0, cell);
        assertForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_XCollision() {
        cell.addBond(cell2);
        cell2.setPosition(1, 0);

        cell.addInterCellForces(cell2);

        assertForce(-1, 0, cell);
        assertForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_YTension() {
        cell.addBond(cell2);
        cell2.setPosition(0, 3);

        cell.addInterCellForces(cell2);

        assertForce(0, 1, cell);
        assertForce(0, -1, cell2);
    }
}
