package fga.evo.model.physics;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class PairCollisionTest {
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
    public void testAddBallPairForces_XRestLength() {
        ball2.setCenterPosition(2, 0);

        PairCollision.addForces(ball, ball2);

        assertNetForce(0, 0, ball);
        assertNetForce(0, 0, ball2);
    }
}
