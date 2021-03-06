package fga.evo.model.util;

import java.util.HashMap;
import java.util.Map;

public class DoubleParameter {
    private double defaultValue;
    private double value;

    public DoubleParameter(double defaultValue) {
        value = this.defaultValue = defaultValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void revertToDefaultValue() {
        setValue(defaultValue);
    }

    public double getDefaultValue() {
        return defaultValue;
    }
}
