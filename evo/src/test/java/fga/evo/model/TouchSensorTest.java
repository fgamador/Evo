package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TouchSensorTest {
    private double defaultRentionRate;

    @Before
    public void setUp() {
        defaultRentionRate = TouchSensor.getRetentionRate();
    }

    @After
    public void tearDown() {
        TouchSensor.setRetentionRate(defaultRentionRate);
    }

    @Test
    public void testNoOverlaps() {
        TouchSensor sensor = new TouchSensor();
        assertEquals(0, sensor.getRecentOverlap(), 0);
    }

    @Test
    public void testOverlaps() {
        TouchSensor sensor = new TouchSensor();
        sensor.addOverlap(1);
        sensor.addOverlap(0.5);

        assertEquals(1.5, sensor.getRecentOverlap(), 0);
    }

    @Test
    public void testDecay() {
        TouchSensor.setRetentionRate(0.75);

        TouchSensor sensor = new TouchSensor();
        sensor.addOverlap(2);
        sensor.endTick();

        assertEquals(1.5, sensor.getRecentOverlap(), 0);
    }
}
