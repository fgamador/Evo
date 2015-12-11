package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OddsTest {
    @Test
    public void testGuaranteedPass() {
        Odds.setNextRandom(0.999);
        assertTrue(Odds.passed(1));
        assertTrue(Odds.passed(10));
    }

    @Test
    public void testGuaranteedFail() {
        Odds.setNextRandom(0);
        assertFalse(Odds.passed(0));
        assertFalse(Odds.passed(-10));
    }

    @Test
    public void testConditionalPass() {
        Odds.setNextRandom(0.49);
        assertTrue(Odds.passed(0.5));
    }

    @Test
    public void testConditionalPass_ExactMatch() {
        Odds.setNextRandom(0.5);
        assertFalse(Odds.passed(0.5));
    }

    @Test
    public void testConditionalFail() {
        Odds.setNextRandom(0.49);
        assertTrue(Odds.passed(0.5));
    }

    @Test
    public void testGoodOddsUsuallyPass() {
        int numPassed = 0;
        for (int i = 0; i < 1000; i++) {
            if (Odds.passed(0.8))
                numPassed++;
        }
        assertEquals(800, numPassed, 100);
    }
}
