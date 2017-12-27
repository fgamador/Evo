package fga.evo.model.physics;

import fga.evo.model.EvoTest;
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
    public void testAddBallPairForces_Bonded_Damping() {
        ball.addBond(ball2);
        ball2.setCenterPosition(2, 0);
        ball2.setVelocity(-1, 0);

        BallPairForces.addBondForces(ball, ball2);

        assertNetForce(-1, 0, ball);
        assertNetForce(1, 0, ball2);
    }
}
