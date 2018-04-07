package fga.evo.model.util;

import org.junit.After;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

public class DecayingAccumulatorTest {
    private DoubleParameter retentionRate = new DoubleParameter(0.95);

    @After
    public void tearDown() {
        retentionRate.revertToDefaultValue();
    }

    @Test
    public void accumulationStartsAtZero() {
        DecayingAccumulator subject = new DecayingAccumulator(retentionRate);
        assertExactEquals(0, subject.getTotal());
    }

    @Test
    public void accumulatedOverlapsSum() {
        DecayingAccumulator subject = new DecayingAccumulator(retentionRate);
        subject.addValue(1);
        subject.addValue(0.5);

        assertExactEquals(1.5, subject.getTotal());
    }

    @Test
    public void accumulationCanDecay() {
        retentionRate.setValue(0.75);

        DecayingAccumulator subject = new DecayingAccumulator(retentionRate);
        subject.addValue(2);
        subject.decay();

        assertApproxEquals(1.5, subject.getTotal());
    }
}
