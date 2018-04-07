package fga.evo.model.util;

import org.junit.Test;

import static fga.evo.model.Assert.assertExactEquals;

public class DoubleParameterTest {
    @Test
    public void hasDefaultValue() {
        DoubleParameter subject = new DoubleParameter(2.5);
        assertExactEquals(2.5, subject.getDefaultValue());
    }

    @Test
    public void valueStartsAsDefaultValue() {
        DoubleParameter subject = new DoubleParameter(2.5);
        assertExactEquals(2.5, subject.getValue());
    }

    @Test
    public void canChangeValue() {
        DoubleParameter subject = new DoubleParameter(2.5);
        subject.setValue(3);
        assertExactEquals(3, subject.getValue());
    }

    @Test
    public void canRevertToDefaultValue() {
        DoubleParameter subject = new DoubleParameter(2.5);
        subject.setValue(3);
        subject.revertToDefaultValue();
        assertExactEquals(2.5, subject.getValue());
    }
}
