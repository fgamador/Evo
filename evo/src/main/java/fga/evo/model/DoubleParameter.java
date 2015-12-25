package fga.evo.model;

import java.util.HashMap;
import java.util.Map;

public class DoubleParameter {
    private static Map<String, DoubleParameter> registeredParameters = new HashMap<>();

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

    public DoubleParameter register(String name) {
        registeredParameters.put(name, this);
        return this;
    }

    public static DoubleParameter getRegistered(String name) {
        return registeredParameters.get(name);
    }
}
