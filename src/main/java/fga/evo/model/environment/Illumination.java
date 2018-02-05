package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.util.DoubleParameter;

/**
 * The light illuminating the cells.
 */
public class Illumination extends EnvironmentalInfluence {
    public static DoubleParameter maxIntensity = new DoubleParameter(1);

    private double depthLimit;

    public Illumination(double depthLimit) {
        setDepthLimit(depthLimit);
    }

    public void setDepthLimit(double depthLimit) {
        if (depthLimit <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depthLimit);
        }

        this.depthLimit = depthLimit;
    }

    @Override
    public void addEnergyToCell(Cell cell) {
        CellEnvironment environment = cell.getEnvironment();
        updateEnvironment(environment);
        cell.photosynthesize();
    }

    public void updateEnvironment(CellEnvironment environment) {
        environment.setLightIntensity(calcLightIntensity(environment.getCenterY()));
    }

    double calcLightIntensity(double y) {
        return (y >= 0) ? maxIntensity.getValue() : maxIntensity.getValue() * (depthLimit + y) / depthLimit;
    }
}
