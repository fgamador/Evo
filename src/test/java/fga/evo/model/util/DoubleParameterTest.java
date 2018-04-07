package fga.evo.model.util;

import org.junit.Test;

import static fga.evo.model.Assert.assertExactEquals;

public class DoubleParameterTest {
    @Test
    public void hasDefaultValue() {
        DoubleParameter testSubject = new DoubleParameter(2.5);
        assertExactEquals(2.5, testSubject.getDefaultValue());
    }

    @Test
    public void valueStartsAsDefaultValue() {
        DoubleParameter testSubject = new DoubleParameter(2.5);
        assertExactEquals(2.5, testSubject.getValue());
    }

    @Test
    public void canChangeValue() {
        DoubleParameter testSubject = new DoubleParameter(2.5);
        testSubject.setValue(3);
        assertExactEquals(3, testSubject.getValue());
    }

    @Test
    public void canRevertToDefaultValue() {
        DoubleParameter testSubject = new DoubleParameter(2.5);
        testSubject.setValue(3);
        testSubject.revertToDefaultValue();
        assertExactEquals(2.5, testSubject.getValue());
    }
}
