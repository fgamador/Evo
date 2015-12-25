package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class DoubleParameterTest {
    private DoubleParameter param;

    @Before
    public void setUp() {
        param = new DoubleParameter(2.5);
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(2.5, param.getValue(), 0);
    }

    @Test
    public void testSetValue() {
        param.setValue(3);
        assertEquals(3, param.getValue(), 0);
    }

    @Test
    public void testRevertToDefaultValue() {
        param.setValue(3);
        param.revertToDefaultValue();
        assertEquals(2.5, param.getValue(), 0);
    }

    @Test
    public void testRegister() {
        assertSame(param, param.register("Param1"));
        assertSame(param, DoubleParameter.getRegistered("Param1"));
    }
}
