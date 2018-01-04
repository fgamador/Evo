package fga.evo.model.util;

import fga.evo.model.util.Chance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChanceTest {
    @Test
    public void testGuaranteedSuccess() {
        Chance.setNextRandom(0.999);
        assertTrue(Chance.success(1));
        assertTrue(Chance.success(10));
    }

    @Test
    public void testGuaranteedFail() {
        Chance.setNextRandom(0);
        assertFalse(Chance.success(0));
        assertFalse(Chance.success(-10));
    }

    @Test
    public void testConditionalSuccess() {
        Chance.setNextRandom(0.49);
        assertTrue(Chance.success(0.5));
    }

    @Test
    public void testConditionalFail() {
        Chance.setNextRandom(0.49);
        assertTrue(Chance.success(0.5));
    }

    @Test
    public void testExactMatch() {
        Chance.setNextRandom(0.5);
        assertFalse(Chance.success(0.5));
    }

    @Test
    public void testGoodOddsUsuallySucceed() {
        int numSucceeded = 0;
        for (int i = 0; i < 1000; i++) {
            if (Chance.success(0.8))
                numSucceeded++;
        }
        assertEquals(800, numSucceeded, 100);
    }
}