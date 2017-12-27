package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class PairBondTest extends EvoTest {
    @Before
    public void setUp() {
        Ball.overlapForceFactor.setValue(1);
        BallPairForces.dampingForceFactor.setValue(1);
    }

    @Test
    public void aPairBondBondsToTheBallsItWasConstructedWith() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        PairBond bond = new PairBond(ball1, ball2);

        assertTrue(bond.bondsTo(ball1));
        assertTrue(bond.bondsTo(ball2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondABallToItself() {
        Ball ball1 = new Ball();
        PairBond bond = new PairBond(ball1, ball1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondNull1() {
        Ball ball = new Ball();
        PairBond bond = new PairBond(null, ball);
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondNull2() {
        Ball ball = new Ball();
        PairBond bond = new PairBond(ball, null);
    }

    @Test
    public void pairBondEqualsUsesBalls() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        PairBond bond1 = new PairBond(ball1, ball2);
        PairBond bond2 = new PairBond(ball1, ball2);

        assertEquals(bond1, bond2);
    }

    @Test
    public void pairBondEqualsIsOrderIndependent() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        PairBond bond1 = new PairBond(ball1, ball2);
        PairBond bond2 = new PairBond(ball2, ball1);

        assertEquals(bond1, bond2);
    }

    @Test
    public void pairBondHashCodeIsOrderIndependent() {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        PairBond bond1 = new PairBond(ball1, ball2);
        PairBond bond2 = new PairBond(ball2, ball1);

        assertEquals(bond1.hashCode(), bond2.hashCode());
    }

    @Test
    public void pairBondAddsNoForcesToBallsJustTouchingAtRest() {
        Ball ball1 = createBall(1, 0, 0);
        Ball ball2 = createBall(1, 2, 0);
        PairBond bond = ball1.addBond(ball2);

        bond.addForces();

        assertNetForce(0, 0, ball1);
        assertNetForce(0, 0, ball2);
    }

    @Test
    public void pairBondAddsNoForcesToBallsJustTouchingMovingTogether() {
        Ball ball1 = createBall(1, 0, 0);
        Ball ball2 = createBall(1, 2, 0);
        PairBond bond = ball1.addBond(ball2);
        ball1.setVelocity(1, 0);
        ball2.setVelocity(1, 0);

        bond.addForces();

        assertNetForce(0, 0, ball1);
        assertNetForce(0, 0, ball2);
    }

    private Ball createBall(int radius, int centerX, int centerY) {
        Ball ball1 = new Ball();
        ball1.setRadius(radius);
        ball1.setCenterPosition(centerX, centerY);
        return ball1;
    }
}