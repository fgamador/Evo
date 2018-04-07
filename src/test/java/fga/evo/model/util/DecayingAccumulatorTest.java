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
        DecayingAccumulator testSubject = new DecayingAccumulator(retentionRate);
        assertExactEquals(0, testSubject.getTotal());
    }

    @Test
    public void accumulatedOverlapsSum() {
        DecayingAccumulator testSubject = new DecayingAccumulator(retentionRate);
        testSubject.addValue(1);
        testSubject.addValue(0.5);

        assertExactEquals(1.5, testSubject.getTotal());
    }

    @Test
    public void accumulationCanDecay() {
        retentionRate.setValue(0.75);

        DecayingAccumulator testSubject = new DecayingAccumulator(retentionRate);
        testSubject.addValue(2);
        testSubject.decay();

        assertApproxEquals(1.5, testSubject.getTotal());
    }
}
