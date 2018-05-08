package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertExactEquals;
import static org.junit.Assert.*;

public class CellPairShadowingTest {
    @Test
    public void overlapShadowsLowerCell() {
        Cell shadower = new Cell.Builder().withRadius(2).withCenterPosition(0, 0).build();
        Cell shadowee = new Cell.Builder().withRadius(1).withCenterPosition(0, -2).build();

        CellPairShadowing.addShadowing(shadower, shadowee);

        assertExactEquals(1, shadower.getEnvironment().getShadowTransmissionFraction());
        assertTrue(shadowee.getEnvironment().getShadowTransmissionFraction() < 1);
    }

    @Test
    public void overlapShadowsLowerCell2() {
        Cell shadower = new Cell.Builder().withRadius(2).withCenterPosition(0, 0).build();
        Cell shadowee = new Cell.Builder().withRadius(1).withCenterPosition(0, -2).build();

        CellPairShadowing.addShadowing(shadowee, shadower);

        assertExactEquals(1, shadower.getEnvironment().getShadowTransmissionFraction());
        assertTrue(shadowee.getEnvironment().getShadowTransmissionFraction() < 1);
    }
}