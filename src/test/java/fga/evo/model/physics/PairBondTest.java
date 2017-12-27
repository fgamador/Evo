package fga.evo.model.physics;

import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class PairBondTest {
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
    public void testAddBallPairForces_Bonded_TouchingAtRest() {
        Ball ball1 = new Ball();
        ball1.setRadius(1);
        ball1.setCenterPosition(0, 0);
        Ball ball2 = new Ball();
        ball2.setRadius(1);
        ball2.setCenterPosition(2, 0);
        PairBond bond = ball1.addBond(ball2);

        bond.addForces();

        assertNetForce(0, 0, ball1);
        assertNetForce(0, 0, ball2);
    }
}