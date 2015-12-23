package fga.evo.model;

public class Parameter {
    private Getter getter;
    private Setter setter;

    public Parameter(Getter getter, Setter setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public double get() {
        return getter.get();
    }

    public void set(double val) {
        setter.set(val);
    }

    public interface Getter {
        double get();
    }

    public interface Setter {
        void set(double val);
    }
}
