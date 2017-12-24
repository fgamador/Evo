package fga.evo.model.physics;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static fga.evo.model.EvoTest.SQRT_2;
import static org.junit.Assert.*;

public class PairCollisionTest {
    @Before
    public void setUp() {
        Ball.overlapForceFactor.setValue(1);
        BallPairForces.dampingForceFactor.setValue(1);
    }

    @Test
    public void justTouchingAddsNoForces() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(2, 0);

        PairCollision.addForces(ball1, ball2);

        assertNetForce(0, 0, ball1);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void xOverlapAddsXForces() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(1, 0);

        PairCollision.addForces(ball1, ball2);

        assertNetForce(-1, 0, ball1);
        assertNetForce(1, 0, ball2);
    }

    @Test
    public void notTouchingAddsNoForces() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(3, -3);

        PairCollision.addForces(ball1, ball2);

        assertNetForce(0, 0, ball1);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void xyOverlapAddsXYForces() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        PairCollision.addForces(ball1, ball2);

        assertNetForce(-SQRT_2 / 2, SQRT_2 / 2, ball1);
        assertNetForce(SQRT_2 / 2, -SQRT_2 / 2, ball2);
    }
}
