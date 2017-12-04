package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RegisteredParametersTest {
    private DoubleParameter param1;

    @Before
    public void setUp() {
        param1 = new DoubleParameter(2);
    }

    @Test
    public void testRegister() {
        RegisteredParameters.register("Param1", param1);
        assertEquals(param1, RegisteredParameters.get("Param1"));
    }

    @Test
    public void testRevertToDefaultValues() {
        RegisteredParameters.register("Param1", param1);
        param1.setValue(param1.getDefaultValue() + 1);

        RegisteredParameters.revertToDefaultValues();

        assertEquals(2, param1.getValue(), 0);
    }
}
