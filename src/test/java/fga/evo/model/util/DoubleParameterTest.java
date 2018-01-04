package fga.evo.model.util;

import fga.evo.model.util.DoubleParameter;
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
        assertEquals(2.5, param.getDefaultValue(), 0);
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
}
