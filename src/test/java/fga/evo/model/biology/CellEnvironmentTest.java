package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

public class CellEnvironmentTest {
    @Test
    public void shadowingMultiplies() {
        CellEnvironment subject = new CellEnvironment();
        subject.addShadowing(0.5);
        subject.addShadowing(0.1);
        assertApproxEquals(0.05, subject.getShadowTransmissionFraction());
    }

    @Test
    public void gettingShadowingResetsIt() {
        CellEnvironment subject = new CellEnvironment();
        subject.addShadowing(0.5);
        assertExactEquals(0.5, subject.getAndResetShadowTransmissionFraction());
        assertExactEquals(1, subject.getShadowTransmissionFraction());
    }
}