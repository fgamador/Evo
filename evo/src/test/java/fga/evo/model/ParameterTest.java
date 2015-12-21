package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParameterTest {
    private static final double paramDefault = 2;
    private double paramValue = paramDefault;

    @Test
    public void testGet() {
        Parameter param = new Parameter(
                () -> paramValue,
                null
        );

        assertEquals(paramDefault, param.get(), 0);
    }

    @Test
    public void testSet() {
        Parameter param = new Parameter(
                null,
                val -> paramValue = val
        );

        double newValue = 3.5;
        param.set(newValue);
        assertEquals(newValue, paramValue, 0);
    }
}
