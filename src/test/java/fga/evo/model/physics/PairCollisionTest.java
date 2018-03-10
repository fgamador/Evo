package fga.evo.model.physics;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static fga.evo.model.EvoTest.SQRT_2;

public class PairCollisionTest {
    @Before
    public void setUp() {
        Ball.overlapForceFactor.setValue(1);
        BallPairForces.dampingForceFactor.setValue(1);
    }

    @Test
    public void xOverlapAddsXForces() {
        Ball ball1 = new BallWithEnvironment();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new BallWithEnvironment();
        ball2.setRadius(1);
        ball2.setCenterPosition(1, 0);

        PairCollision.addForces(ball1, ball2, 1);

        assertNetForce((double) -1, (double) 0, ball1.getEnvironment());
        assertNetForce((double) 1, (double) 0, ball2.getEnvironment());
    }

    @Test
    public void xyOverlapAddsXYForces() {
        Ball ball1 = new BallWithEnvironment();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new BallWithEnvironment();
        ball2.setRadius(1);
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        PairCollision.addForces(ball1, ball2, 1);

        double forceX = -SQRT_2 / 2;
        assertNetForce(forceX, SQRT_2 / 2, ball1.getEnvironment());
        double forceY = -SQRT_2 / 2;
        assertNetForce(SQRT_2 / 2, forceY, ball2.getEnvironment());
    }

    @Test
    public void reversingBallOrderMakesNoDifference() {
        Ball ball1 = new BallWithEnvironment();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new BallWithEnvironment();
        ball2.setRadius(1);
        ball2.setCenterPosition(1 / SQRT_2, -1 / SQRT_2);

        PairCollision.addForces(ball2, ball1, 1);

        double forceX = -SQRT_2 / 2;
        assertNetForce(forceX, SQRT_2 / 2, ball1.getEnvironment());
        double forceY = -SQRT_2 / 2;
        assertNetForce(SQRT_2 / 2, forceY, ball2.getEnvironment());
    }

    @Test
    public void overlapWithSameCenterPositionAddsNoForces() {
        Ball ball1 = new BallWithEnvironment();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);

        Ball ball2 = new BallWithEnvironment();
        ball2.setRadius(1);
        ball2.setCenterPosition(0, 0);

        PairCollision.addForces(ball1, ball2, 2);

        assertNetForce((double) 0, (double) 0, ball1.getEnvironment());
        assertNetForce((double) 0, (double) 0, ball2.getEnvironment());
    }
}
