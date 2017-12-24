package fga.evo.model.physics;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class PairCollisionTest {
    @Before
    public void setUp() {
        Ball.overlapForceFactor.setValue(1);
        BallPairForces.dampingForceFactor.setValue(1);
    }

    @Test
    public void grazingCollisionAddsNoForces() {
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
}
