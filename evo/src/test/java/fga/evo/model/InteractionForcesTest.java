package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class InteractionForcesTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private SimpleBall ball; //, ball2;
    private Cell cell, cell2;

    @Before
    public void setUp() {
        ball = new SimpleBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
        cell = new Cell(1);
        cell2 = new Cell(1);
        cell.setCenterPosition(0, 0);
    }

    @Test
    public void testCalcOverlapForce() {
        double defaultOverlapForceFactor = InteractionForces.getOverlapForceFactor();
        try {
            InteractionForces.setOverlapForceFactor(2);
            assertEquals(2, InteractionForces.calcOverlapForce(1), 0);
        } finally {
            InteractionForces.setOverlapForceFactor(defaultOverlapForceFactor);
        }
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        ball.setCenterPosition(5, 0);

        assertEquals(0, InteractionForces.calcMinXWallCollisionForce(ball, 0), 0); // no contact
        assertEquals(0, InteractionForces.calcMinXWallCollisionForce(ball, 4), 0); // just touching
        assertEquals(0.5, InteractionForces.calcMinXWallCollisionForce(ball, 4.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        ball.setCenterPosition(5, 0);

        assertEquals(0, InteractionForces.calcMaxXWallCollisionForce(ball, 10), 0); // no contact
        assertEquals(0, InteractionForces.calcMaxXWallCollisionForce(ball, 6), 0); // just touching
        assertEquals(-0.5, InteractionForces.calcMaxXWallCollisionForce(ball, 5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        ball.setCenterPosition(0, -5);

        assertEquals(0, InteractionForces.calcMinYWallCollisionForce(ball, -10), 0); // no contact
        assertEquals(0, InteractionForces.calcMinYWallCollisionForce(ball, -6), 0); // just touching
        assertEquals(0.5, InteractionForces.calcMinYWallCollisionForce(ball, -5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        ball.setCenterPosition(0, -5);

        assertEquals(0, InteractionForces.calcMaxYWallCollisionForce(ball, 0), 0); // no contact
        assertEquals(0, InteractionForces.calcMaxYWallCollisionForce(ball, -4), 0); // just touching
        assertEquals(-0.5, InteractionForces.calcMaxYWallCollisionForce(ball, -4.5), 0); // overlap by 0.5
    }

    @Test
    public void testAddInterCellForces_XRestLength() {
        cell2.setCenterPosition(2, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_XCollision() {
        cell2.setCenterPosition(1, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_NotInCollision() {
        cell2.setCenterPosition(3, -3);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_DiagonalCollision() {
        cell2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_ReverseDiagonalCollision() {
        cell2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        InteractionForces.addInterCellForces(cell2, cell);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, cell);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, cell2);
    }

    @Test
    public void testAddInterCellForces_FullOverlap() {
        cell2.setCenterPosition(0, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingAtRest() {
        cell.addBond(cell2);
        cell2.setCenterPosition(2, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_TouchingMovingTogether() {
        cell.addBond(cell2);
        cell2.setCenterPosition(2, 0);
        cell.setVelocity(1, 0);
        cell2.setVelocity(1, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, 0, cell);
        assertNetForce(0, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_XCollision() {
        cell.addBond(cell2);
        cell2.setCenterPosition(1, 0);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(-1, 0, cell);
        assertNetForce(1, 0, cell2);
    }

    @Test
    public void testAddInterCellForces_Bonded_YTension() {
        cell.addBond(cell2);
        cell2.setCenterPosition(0, -3);

        InteractionForces.addInterCellForces(cell, cell2);

        assertNetForce(0, -1, cell);
        assertNetForce(0, 1, cell2);
    }
}
