package fga.evo.model;

import fga.evo.model.physics.NewtonianBody;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testSubtickPhysics_OneForce() {
        ball.addForce(0.5, -1);

        ball.subtickPhysics(1);

        assertVelocity(0.5, -1, ball);
        assertPosition(0.5, -1, ball);
    }

    @Test
    public void testSubtickPhysics_TwoForces() {
        ball.addForce(0.5, -1);
        ball.addForce(1.5, 2);

        ball.subtickPhysics(1);

        assertVelocity(2, 1, ball);
        assertPosition(2, 1, ball);
    }

    @Test
    public void testSubtickPhysics_Coasting() {
        ball.addForce(0.5, -1);
        ball.subtickPhysics(1);

        ball.subtickPhysics(1);

        assertVelocity(0.5, -1, ball);
        assertPosition(1, -2, ball);
    }

    @Test
    public void testSubtickPhysics_DoubleMass() {
        Ball heavyBall = new Ball();
        heavyBall.setRadius(1);
        heavyBall.setMass(2);
        heavyBall.addForce(1, -2);

        heavyBall.subtickPhysics(1);

        assertVelocity(0.5, -1, heavyBall);
        assertPosition(0.5, -1, heavyBall);
    }

    @Test
    public void testSubtickPhysics_SpeedLimit() {
        NewtonianBody.speedLimit.setValue(4);
        ball.setVelocity(8 / SQRT_2, -8 / SQRT_2);

        ball.subtickPhysics(1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, ball);
    }

    @Test
    public void testSubtickPhysics_DoubleResolution_ConstantVelocity() {
        ball.setVelocity(1, 1);

        ball.subtickPhysics(2);

        assertVelocity(1, 1, ball);
        assertPosition(0.5, 0.5, ball);

        ball.subtickPhysics(2);

        assertVelocity(1, 1, ball);
        assertPosition(1, 1, ball);
    }

    @Test
    public void testSubtickPhysics_DoubleResolution_ConstantForce() {
        ball.addForce(1, 1);
        ball.subtickPhysics(2);

        assertVelocity(0.5, 0.5, ball);
        assertPosition(0.25, 0.25, ball);

        ball.addForce(1, 1);
        ball.subtickPhysics(2);

        assertVelocity(1, 1, ball);
        assertPosition(0.75, 0.75, ball);
    }

    @Test
    public void testGetRecentTotalOverlap() {
        Ball ball1 = new Cell(1);
        ball1.setCenterPosition(0, 0);
        Ball ball2 = new Cell(1);
        ball2.setCenterPosition(1.5, 0);

        ball1.addBallPairForces(ball2);

        assertEquals(0.5, ball1.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);

        ball1.subtickPhysics(2);
        ball2.subtickPhysics(2);
        ball1.addBallPairForces(ball2);

        assertTrue(ball1.getRecentTotalOverlap() < 1);
        assertEquals(ball1.getRecentTotalOverlap(), ball2.getRecentTotalOverlap(), 0);
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
