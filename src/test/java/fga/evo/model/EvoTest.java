package fga.evo.model;

import org.junit.After;

public class EvoTest {
    public static final double SQRT_2 = Math.sqrt(2);

    @After
    public void revertParameters() {
        RegisteredParameters.revertToDefaultValues();
    }
}
