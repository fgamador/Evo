package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ParameterTest {
    private static final double paramDefault = 2;
    private double paramValue = paramDefault;

    @Test
    public void testGetValue() {
        Parameter param = new Parameter(
                () -> paramValue,
                null
        );

        assertEquals(paramDefault, param.getValue(), 0);
    }

    @Test
    public void testSetValue() {
        Parameter param = new Parameter(
                null,
                val -> paramValue = val
        );

        double newValue = 3.5;
        param.setValue(newValue);
        assertEquals(newValue, paramValue, 0);
    }

    @Test
    public void testRegister() {
        Parameter param = new Parameter(null, null);
        assertSame(param, param.register("Param1"));
        assertSame(param, Parameter.getRegistered("Param1"));
    }
}
