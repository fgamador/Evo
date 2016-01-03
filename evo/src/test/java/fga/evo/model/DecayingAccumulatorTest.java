package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DecayingAccumulatorTest {
    private DoubleParameter retentionRate = new DoubleParameter(0.95);

    @After
    public void tearDown() {
        retentionRate.revertToDefaultValue();
    }

    @Test
    public void testNoOverlaps() {
        DecayingAccumulator sensor = new DecayingAccumulator(retentionRate);
        assertEquals(0, sensor.getTotal(), 0);
    }

    @Test
    public void testOverlaps() {
        DecayingAccumulator sensor = new DecayingAccumulator(retentionRate);
        sensor.addValue(1);
        sensor.addValue(0.5);

        assertEquals(1.5, sensor.getTotal(), 0);
    }

    @Test
    public void testDecay() {
        retentionRate.setValue(0.75);

        DecayingAccumulator sensor = new DecayingAccumulator(retentionRate);
        sensor.addValue(2);
        sensor.decay();

        assertEquals(1.5, sensor.getTotal(), 0);
    }
}
