package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import fga.evo.model.physics.Ball;
import fga.evo.model.physics.BallPairForces;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class BallPairForcesTest extends EvoTest {
    private Ball ball, ball2;

    @Before
    public void setUp() {
        Ball.overlapForceFactor.setValue(1);
        BallPairForces.dampingForceFactor.setValue(1);

        ball = new Ball();
        ball.setRadius(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);

        ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setMass(1);
    }

    @Test
    public void testCalcOverlapForce() {
        Ball.overlapForceFactor.setValue(2);
        assertEquals(2, Ball.calcOverlapForce(1), 0);
    }

    @Test
    public void testAddBallPairForces_XRestLength() {
        ball2.setCenterPosition(2, 0);

        BallPairForces.addCollisionForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_XCollision() {
        ball2.setCenterPosition(1, 0);

        BallPairForces.addCollisionForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_NotInCollision() {
        ball2.setCenterPosition(3, -3);

        BallPairForces.addCollisionForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_DiagonalCollision() {
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        BallPairForces.addCollisionForces(ball, ball2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, ball);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, ball2);
    }

    @Test
    public void testAddBallPairForces_ReverseDiagonalCollision() {
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        BallPairForces.addCollisionForces(ball2, ball);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, ball);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, ball2);
    }

    @Test
    public void testAddBallPairForces_FullOverlap() {
        ball2.setCenterPosition(0, 0);

        BallPairForces.addCollisionForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_Bonded_TouchingAtRest() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_Bonded_TouchingMovingTogether() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);
        ball.setVelocity(1, 0);
        ball2.setVelocity(1, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_Bonded_XCollision() {
        ball.addBond(ball2);
        ball2.setCenterPosition(1, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testAddBallPairForces_Bonded_YTension() {
        ball.addBond(ball2);
        ball2.setCenterPosition(0, -3);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(0, -1, ball);
        assertNetForce(0, 1, ball2);
    }

    @Test
    public void testAddBallPairForces_Bonded_Damping() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);
        ball2.setVelocity(-1, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void testOnOverlap_BallCollision() {
        ball2.setCenterPosition(1.5, 0);

        BallPairForces.addCollisionForces(ball, ball2);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testOnOverlap_BondedBallCollision() {
        ball.addBond(ball2);
        ball2.setCenterPosition(1.5, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testOnOverlap_BondedBallExtension() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2.5, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertEquals(0, ball.getRecentTotalOverlap(), 0);
        assertEquals(0, ball2.getRecentTotalOverlap(), 0);
    }
}
