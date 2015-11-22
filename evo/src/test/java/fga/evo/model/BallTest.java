package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;

public class BallTest {
    public static final double SQRT_2 = Math.sqrt(2);

    private SimpleBall ball; //, ball2;

    @Before
    public void setUp() {
        ball = new SimpleBall(1);
        ball.setMass(1);
        ball.setCenterPosition(0, 0);
//        ball2 = new SimpleBall(1);
//        ball2.setMass(1);
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
        SimpleBall heavyBall = new SimpleBall(SQRT_2);
        heavyBall.setMass(2);
        heavyBall.addForce(1, -2);

        heavyBall.move();

        assertVelocity(0.5, -1, heavyBall);
        assertPosition(0.5, -1, heavyBall);
    }

    @Test
    public void testMove_SpeedLimit() {
        ball.setVelocity(8 / SQRT_2, -8 / SQRT_2);

        ball.move();

        assertEquals(4, Ball.getSpeedLimit(), 0);
        assertVelocity(4 / SQRT_2, -4 / SQRT_2, ball);
    }

    static class SimpleBall extends Ball {
        private double mass;
        private double radius;

        SimpleBall(double radius) {
            setRadius(radius);
        }

        public void setMass(double val) {
            mass = val;
        }

        public void setRadius(double val) {
            radius = val;
        }

        public double getMass() {
            return mass;
        }

        public double getRadius() {
            return radius;
        }
    }
}
