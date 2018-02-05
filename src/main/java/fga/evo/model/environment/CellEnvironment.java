package fga.evo.model.environment;

public abstract class CellEnvironment {
    private double lightIntensity;

    public abstract double getCenterX();

    public abstract double getCenterY();

    public double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(double val) {
        lightIntensity = val;
    }
}
