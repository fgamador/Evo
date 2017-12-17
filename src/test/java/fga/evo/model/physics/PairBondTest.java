package fga.evo.model.physics;

import org.junit.Test;

import static org.junit.Assert.*;

public class PairBondTest {
    @Test
    public void aPairBondBondsToTheBodiesItWasConstructedWith() {
        NewtonianBody body1 = new NewtonianBody();
        NewtonianBody body2 = new NewtonianBody();

        PairBond bond = new PairBond(body1, body2);

        assertTrue(bond.bondsTo(body1));
        assertTrue(bond.bondsTo(body2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondABodyToItself() {
        NewtonianBody body1 = new NewtonianBody();
        PairBond bond = new PairBond(body1, body1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondNull1() {
        NewtonianBody body = new NewtonianBody();
        PairBond bond = new PairBond(null, body);
    }

    @Test(expected = IllegalArgumentException.class)
    public void aPairBondCannotBondNull2() {
        NewtonianBody body = new NewtonianBody();
        PairBond bond = new PairBond(body, null);
    }

    @Test
    public void pairBondEqualsUsesBodies() {
        NewtonianBody body1 = new NewtonianBody();
        NewtonianBody body2 = new NewtonianBody();

        PairBond bond1 = new PairBond(body1, body2);
        PairBond bond2 = new PairBond(body1, body2);

        assertEquals(bond1, bond2);
    }

    @Test
    public void pairBondEqualsIsOrderIndependent() {
        NewtonianBody body1 = new NewtonianBody();
        NewtonianBody body2 = new NewtonianBody();

        PairBond bond1 = new PairBond(body1, body2);
        PairBond bond2 = new PairBond(body2, body1);

        assertEquals(bond1, bond2);
    }

    @Test
    public void pairBondHashCodeIsOrderIndependent() {
        NewtonianBody body1 = new NewtonianBody();
        NewtonianBody body2 = new NewtonianBody();

        PairBond bond1 = new PairBond(body1, body2);
        PairBond bond2 = new PairBond(body2, body1);

        assertEquals(bond1.hashCode(), bond2.hashCode());
    }
}