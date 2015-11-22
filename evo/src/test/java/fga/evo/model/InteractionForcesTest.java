package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InteractionForcesTest {
    private BallTest.SimpleBall ball; //, ball2;

    @Before
    public void setUp() {
        ball = new BallTest.SimpleBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
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
}
