package fga.evo.model;

import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private static Map<String, Parameter> registeredParameters = new HashMap<>();

    private Getter getter;
    private Setter setter;

    public Parameter(Getter getter, Setter setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public Parameter register(String name) {
        registeredParameters.put(name, this);
        return this;
    }

    public double getValue() {
        return getter.get();
    }

    public void setValue(double val) {
        setter.set(val);
    }

    public static Parameter getRegistered(String name) {
        return registeredParameters.get(name);
    }

    public interface Getter {
        double get();
    }

    public interface Setter {
        void set(double val);
    }
}
