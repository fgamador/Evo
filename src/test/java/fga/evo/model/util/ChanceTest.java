package fga.evo.model.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChanceTest {
    @Test
    public void guaranteedSuccess() {
        Chance.setNextRandom(0.999);
        assertTrue(Chance.beats(1));
        assertTrue(Chance.beats(10));
    }

    @Test
    public void guaranteedFail() {
        Chance.setNextRandom(0);
        assertFalse(Chance.beats(0));
        assertFalse(Chance.beats(-10));
    }

    @Test
    public void conditionalSuccess() {
        Chance.setNextRandom(0.49);
        assertTrue(Chance.beats(0.5));
    }

    @Test
    public void conditionalFail() {
        Chance.setNextRandom(0.49);
        assertTrue(Chance.beats(0.5));
    }

    @Test
    public void beatsExactMatchOdds() {
        Chance.setNextRandom(0.5);
        assertFalse(Chance.beats(0.5));
    }

    @Test
    public void goodOddsUsuallySucceed() {
        int numSucceeded = 0;
        for (int i = 0; i < 1000; i++) {
            if (Chance.beats(0.8))
                numSucceeded++;
        }
        assertEquals(800, numSucceeded, 100);
    }
}
