package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;

public class BallTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private Ball ball, ball2;

    @Before
    public void setUp() {
        ball = new Ball(1);
        ball.setMass(1);
        ball2 = new Ball(1);
        ball2.setMass(1);
        ball.setCenterPosition(0, 0);
    }

    @Test
    public void testGetArea() {
        Ball bigBall = new Ball(5);
        assertEquals(Math.PI * 25, bigBall.getArea(), 0);
    }

    @Test
    public void testMove_NoForces() {
        ball.move();

        assertVelocity(0, 0, ball);
        assertPosition(0, 0, ball);
    }

    @Test
    public void testMove_OneForce() {
        ball.addForce(0.5, -1);

        ball.move();

        assertVelocity(0.5, -1, ball);
        assertPosition(0.5, -1, ball);
    }

    @Test
    public void testMove_TwoForces() {
        ball.addForce(0.5, -1);
        ball.addForce(1.5, 2);

        ball.move();

        assertVelocity(2, 1, ball);
        assertPosition(2, 1, ball);
    }

    @Test
    public void testMove_Coasting() {
        ball.addForce(0.5, -1);
        ball.move();

        ball.move();

        assertVelocity(0.5, -1, ball);
        assertPosition(1, -2, ball);
    }

    @Test
    public void testMove_DoubleMass() {
        Cell heavyCell = new Cell(SQRT_2);
        heavyCell.setMass(2);
        heavyCell.addForce(1, -2);

        heavyCell.move();

        assertVelocity(0.5, -1, heavyCell);
        assertPosition(0.5, -1, heavyCell);
    }

    @Test
    public void testMove_SpeedLimit() {
        ball.setVelocity(8 / SQRT_2, -8 / SQRT_2);

        ball.move();

        assertEquals(4, Ball.getSpeedLimit(), 0);
        assertVelocity(4 / SQRT_2, -4 / SQRT_2, ball);
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        ball.setCenterPosition(5, 0);

        assertEquals(0, ball.calcMinXWallCollisionForce(0), 0); // no contact
        assertEquals(0, ball.calcMinXWallCollisionForce(4), 0); // just touching
        assertEquals(0.5, ball.calcMinXWallCollisionForce(4.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        ball.setCenterPosition(5, 0);

        assertEquals(0, ball.calcMaxXWallCollisionForce(10), 0); // no contact
        assertEquals(0, ball.calcMaxXWallCollisionForce(6), 0); // just touching
        assertEquals(-0.5, ball.calcMaxXWallCollisionForce(5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        ball.setCenterPosition(0, -5);

        assertEquals(0, ball.calcMinYWallCollisionForce(-10), 0); // no contact
        assertEquals(0, ball.calcMinYWallCollisionForce(-6), 0); // just touching
        assertEquals(0.5, ball.calcMinYWallCollisionForce(-5.5), 0); // overlap by 0.5
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        ball.setCenterPosition(0, -5);

        assertEquals(0, ball.calcMaxYWallCollisionForce(0), 0); // no contact
        assertEquals(0, ball.calcMaxYWallCollisionForce(-4), 0); // just touching
        assertEquals(-0.5, ball.calcMaxYWallCollisionForce(-4.5), 0); // overlap by 0.5
    }
}
