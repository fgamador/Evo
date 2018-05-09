package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

public class CellPairShadowingTest {
    @Test
    public void overlapShadowsLowerCell() {
        Cell shadower = new Cell.Builder().withRadius(2).withCenterPosition(0, 0).build();
        Cell shadowee = new Cell.Builder().withRadius(1).withCenterPosition(0, -2).build();

        CellPairShadowing.addShadowingToLowerCell(shadower, shadowee);

        assertExactEquals(1, shadower.getEnvironment().getShadowTransmissionFraction());
        assertExactEquals(shadower.getShadowTransmissionFraction(), shadowee.getEnvironment().getShadowTransmissionFraction());
    }

    @Test
    public void overlapShadowsLowerCell2() {
        Cell shadower = new Cell.Builder().withRadius(2).withCenterPosition(0, 0).build();
        Cell shadowee = new Cell.Builder().withRadius(1).withCenterPosition(0, -2).build();

        CellPairShadowing.addShadowingToLowerCell(shadowee, shadower);

        assertExactEquals(1, shadower.getEnvironment().getShadowTransmissionFraction());
        assertExactEquals(shadower.getShadowTransmissionFraction(), shadowee.getEnvironment().getShadowTransmissionFraction());
    }

    @Test
    public void partialOverlapIncreasesTransmissionFactor() {
        Cell shadower = new Cell.Builder().withRadius(2).withCenterPosition(0, 0).build();
        Cell shadowee = new Cell.Builder().withRadius(2).withCenterPosition(3, -4).build();

        CellPairShadowing.addShadowingToLowerCell(shadowee, shadower);

        double quarterOverlapTransmissionFactor = 0.75 + 0.25 * shadower.getShadowTransmissionFraction();
        assertApproxEquals(quarterOverlapTransmissionFactor, shadowee.getEnvironment().getShadowTransmissionFraction());
    }
}
