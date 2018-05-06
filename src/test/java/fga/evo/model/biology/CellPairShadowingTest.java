package fga.evo.model.biology;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellPairShadowingTest {
    @Test
    public void totalOverlap() {
        Cell shadower = new Cell(2);
        shadower.setCenterPosition(0, 0);
        Cell shadowee = new Cell(1);
        shadowee.setCenterPosition(0, -2);

        CellPairShadowing.addShadowing(shadower, shadowee);
    }
}