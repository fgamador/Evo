package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class BallForcesTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private TestBall ball, ball2;

    @Before
    public void setUp() {
        BallForces.overlapForceFactor.setValue(1);
        BallForces.dampingForceFactor.setValue(1);

        ball = new TestBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
        ball2 = new TestBall(1);
        ball2.setMass(1);
    }

    @After
    public void tearDown() {
        BallForces.overlapForceFactor.revertToDefaultValue();
    }

    @Test
    public void testCalcOverlapForce() {
        BallForces.overlapForceFactor.setValue(2);
        assertEquals(2, BallForces.calcOverlapForce(1), 0);
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
