package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertExactEquals;
import static org.junit.Assert.*;

public class CellPairShadowingTest {
    @Test
    public void totalOverlap() {
        Cell shadower = new Cell(2);
        shadower.setCenterPosition(0, 0);
        Cell shadowee = new Cell(1);
        shadowee.setCenterPosition(0, -2);

        CellPairShadowing.addShadowing(shadower, shadowee);

        assertExactEquals(1, shadower.getEnvironment().getShadowTransmissionFraction());
        assertTrue(shadowee.getEnvironment().getShadowTransmissionFraction() < 1);
    }
}