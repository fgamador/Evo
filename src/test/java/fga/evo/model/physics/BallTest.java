package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import fga.evo.model.geometry.Circles;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class BallTest extends EvoTest {
    private Ball ball;

    @Before
    public void setUp() {
        NewtonianBody.speedLimit.setValue(100);

        ball = new Ball();
        ball.setRadius(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
    }

    @Test
    public void testSetRadius() {
        assertEquals(Math.PI, ball.getArea(), 0.001);
    }

    @Test
    public void testGetArea() {
        ball.setRadius(5);
        assertEquals(25 * Math.PI, ball.getArea(), 0.001);
    }

    @Test
    public void testCalcOverlapForce() {
        Ball.overlapForceFactor.setValue(2);
        assertEquals(2, Ball.calcOverlapForce(1), 0);
    }

    @Test
    public void unbondedBallsAreNotBonded() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void bondingBallsBondsThem() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        ball1.addBond(ball2);

        assertTrue(ball1.isBondedTo(ball2));
        assertTrue(ball2.isBondedTo(ball1));
    }

    @Test
    public void bondingBallsReturnsBond() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        PairBond bond = ball1.addBond(ball2);

        assertTrue(bond.bondsTo(ball1));
        assertTrue(bond.bondsTo(ball2));
    }

    @Test
    public void unbondingBallsUnbondsThem() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        ball1.addBond(ball2);

        ball1.removeBond(ball2);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void unbondingBallsReturnsBond() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        ball1.addBond(ball2);

        PairBond bond = ball1.removeBond(ball2);

        assertTrue(bond.bondsTo(ball1));
        assertTrue(bond.bondsTo(ball2));
    }

    @Test
    public void unbondingBallsFromOtherEndUnbondsThem() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        ball1.addBond(ball2);

        ball2.removeBond(ball1);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void unbondingRemovesOnlySpecifiedBond() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        Ball ball3 = new Ball();
        ball1.addBond(ball2);
        ball1.addBond(ball3);

        ball1.removeBond(ball2);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
        assertTrue(ball1.isBondedTo(ball3));
        assertTrue(ball3.isBondedTo(ball1));
    }

    @Test(expected = IllegalStateException.class)
    public void cannotBondSameBallTwice() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        ball1.addBond(ball2);
        ball1.addBond(ball2);
    }

    @Test(expected = IllegalStateException.class)
    public void cannotBondSameBallTwiceEvenFromOtherEnd() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();
        ball1.addBond(ball2);
        ball2.addBond(ball1);
    }

    @Test
    public void testGetRecentTotalOverlap() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setMass(1);
        ball1.setCenterPosition(0, 0);
        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setMass(1);
        ball2.setCenterPosition(1.5, 0);

        ball1.onOverlap(ball2, 0.5);

        assertEquals(0.5, ball1.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);

        ball1.subtickPhysics(2);
        ball2.subtickPhysics(2);
        ball1.onOverlap(ball2, Circles.calcOverlap(ball1, ball2));

        assertTrue(ball1.getRecentTotalOverlap() < 1);
        assertEquals(ball1.getRecentTotalOverlap(), ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void recordOverlapEvenIfBonded() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);
        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(1.5, 0);
        ball1.addBond(ball2);

        ball1.onOverlap(ball2, 0.5);

        assertEquals(0.5, ball1.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testOnOverlap_LeftWallCollision() {
        ball.addLeftBarrierCollisionForce(-0.5);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        checkAddLeftBarrierCollisionForce(ball, 5, 0, 0); // no contact
        checkAddLeftBarrierCollisionForce(ball, 5, 4, 0); // just touching
        checkAddLeftBarrierCollisionForce(ball, 5, 4.5, 0.5); // overlap by 0.5
    }

    private void checkAddLeftBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);

        ball.addLeftBarrierCollisionForce(wallX);

        assertNetForce(expected, 0, ball);
    }

    @Test
    public void testOnOverlap_RightWallCollision() {
        ball.addRightBarrierCollisionForce(0.5);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        checkAddRightBarrierCollisionForce(ball, 5, 10, 0); // no contact
        checkAddRightBarrierCollisionForce(ball, 5, 6, 0); // just touching
        checkAddRightBarrierCollisionForce(ball, 5, 5.5, -0.5); // overlap by 0.5
    }

    private void checkAddRightBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);

        ball.addRightBarrierCollisionForce(wallX);

        assertNetForce(expected, 0, ball);
    }

    @Test
    public void testOnOverlap_LowWallCollision() {
        ball.addLowBarrierCollisionForce(-0.5);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        checkAddLowBarrierCollisionForce(ball, -5, -10, 0); // no contact
        checkAddLowBarrierCollisionForce(ball, -5, -6, 0); // just touching
        checkAddLowBarrierCollisionForce(ball, -5, -5.5, 0.5); // overlap by 0.5
    }

    private void checkAddLowBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);

        ball.addLowBarrierCollisionForce(wallY);

        assertNetForce(0, expected, ball);
    }

    @Test
    public void testOnOverlap_HighWallCollision() {
        ball.addHighBarrierCollisionForce(0.5);

        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        checkAddHighBarrierCollisionForce(ball, -5, 0, 0); // no contact
        checkAddHighBarrierCollisionForce(ball, -5, -4, 0); // just touching
        checkAddHighBarrierCollisionForce(ball, -5, -4.5, -0.5); // overlap by 0.5
    }

    private void checkAddHighBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);

        ball.addHighBarrierCollisionForce(wallY);

        assertNetForce(0, expected, ball);
    }
}
