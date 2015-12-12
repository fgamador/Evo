package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class BallForcesTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private SimpleBall ball, ball2;

    @Before
    public void setUp() {
        ball = new SimpleBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
        ball2 = new SimpleBall(1);
        ball2.setMass(1);
    }

    @Test
    public void testCalcOverlapForce() {
        double defaultOverlapForceFactor = BallForces.getOverlapForceFactor();
        try {
            BallForces.setOverlapForceFactor(2);
            assertEquals(2, BallForces.calcOverlapForce(1), 0);
        } finally {
            BallForces.setOverlapForceFactor(defaultOverlapForceFactor);
        }
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        checkAddLeftBarrierCollisionForce(ball, 5, 0, 0); // no contact
        checkAddLeftBarrierCollisionForce(ball, 5, 4, 0); // just touching
        checkAddLeftBarrierCollisionForce(ball, 5, 4.5, 0.5); // overlap by 0.5
    }

    private void checkAddLeftBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);

        BallForces.addLeftBarrierCollisionForce(ball, wallX);

        assertNetForce(expected, 0, ball);
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        checkAddRightBarrierCollisionForce(ball, 5, 10, 0); // no contact
        checkAddRightBarrierCollisionForce(ball, 5, 6, 0); // just touching
        checkAddRightBarrierCollisionForce(ball, 5, 5.5, -0.5); // overlap by 0.5
    }

    private void checkAddRightBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);

        BallForces.addRightBarrierCollisionForce(ball, wallX);

        assertNetForce(expected, 0, ball);
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        checkAddLowBarrierCollisionForce(ball, -5, -10, 0); // no contact
        checkAddLowBarrierCollisionForce(ball, -5, -6, 0); // just touching
        checkAddLowBarrierCollisionForce(ball, -5, -5.5, 0.5); // overlap by 0.5
    }

    private void checkAddLowBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);

        BallForces.addLowBarrierCollisionForce(ball, wallY);

        assertNetForce(0, expected, ball);
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        checkAddHighBarrierCollisionForce(ball, -5, 0, 0); // no contact
        checkAddHighBarrierCollisionForce(ball, -5, -4, 0); // just touching
        checkAddHighBarrierCollisionForce(ball, -5, -4.5, -0.5); // overlap by 0.5
    }

    private void checkAddHighBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);

        BallForces.addHighBarrierCollisionForce(ball, wallY);

        assertNetForce(0, expected, ball);
    }

    @Test
    public void testAddInterBallForces_XRestLength() {
        ball2.setCenterPosition(2, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_XCollision() {
        ball2.setCenterPosition(1, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_NotInCollision() {
        ball2.setCenterPosition(3, -3);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_DiagonalCollision() {
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, ball);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, ball2);
    }

    @Test
    public void testAddInterBallForces_ReverseDiagonalCollision() {
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        BallForces.addInterBallForces(ball2, ball);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, ball);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, ball2);
    }

    @Test
    public void testAddInterBallForces_FullOverlap() {
        ball2.setCenterPosition(0, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_Bonded_TouchingAtRest() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_Bonded_TouchingMovingTogether() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);
        ball.setVelocity(1, 0);
        ball2.setVelocity(1, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_Bonded_XCollision() {
        ball.addBond(ball2);
        ball2.setCenterPosition(1, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testAddInterBallForces_Bonded_YTension() {
        ball.addBond(ball2);
        ball2.setCenterPosition(0, -3);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(0, -1, ball);
        assertNetForce(0, 1, ball2);
    }

    @Test
    public void testAddInterBallForces_Bonded_Damping() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);
        ball2.setVelocity(-1, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testOnOverlap_LeftWallCollision() {
        BallForces.addLeftBarrierCollisionForce(ball, -0.5);

        assertEquals(0.5, ball.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_RightWallCollision() {
        BallForces.addRightBarrierCollisionForce(ball, 0.5);

        assertEquals(0.5, ball.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_LowWallCollision() {
        BallForces.addLowBarrierCollisionForce(ball, -0.5);

        assertEquals(0.5, ball.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_HighWallCollision() {
        BallForces.addHighBarrierCollisionForce(ball, 0.5);

        assertEquals(0.5, ball.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_BallCollision() {
        ball2.setCenterPosition(1.5, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertEquals(0.5, ball.getLastOverlap(), 0);
        assertEquals(0.5, ball2.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_BondedBallCollision() {
        ball.addBond(ball2);
        ball2.setCenterPosition(1.5, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertEquals(0.5, ball.getLastOverlap(), 0);
        assertEquals(0.5, ball2.getLastOverlap(), 0);
    }

    @Test
    public void testOnOverlap_BondedBallExtension() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2.5, 0);

        BallForces.addInterBallForces(ball, ball2);

        assertEquals(0, ball.getLastOverlap(), 0);
        assertEquals(0, ball2.getLastOverlap(), 0);
    }
}
