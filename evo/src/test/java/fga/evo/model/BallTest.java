package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;

public class BallTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private SimpleBall ball;

    @Before
    public void setUp() {
        ball = new SimpleBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
    }

    @After
    public void tearDown() {
        Ball.speedLimit.revertToDefaultValue();
    }

    @Test
    public void testSubtickPhysics_NoForces() {
        ball.subtickPhysics(1);

        assertVelocity(0, 0, ball);
        assertPosition(0, 0, ball);
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
        SimpleBall heavyBall = new SimpleBall(SQRT_2);
        heavyBall.setMass(2);
        heavyBall.addForce(1, -2);

        heavyBall.subtickPhysics(1);

        assertVelocity(0.5, -1, heavyBall);
        assertPosition(0.5, -1, heavyBall);
    }

    @Test
    public void testSubtickPhysics_SpeedLimit() {
        Ball.speedLimit.setValue(4);
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
}
