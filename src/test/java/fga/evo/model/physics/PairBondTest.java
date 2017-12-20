package fga.evo.model.physics;

import org.junit.Test;

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
}