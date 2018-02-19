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

    // TODO almost obsolete
    @Override
    public void addEnergyToCell(Cell cell) {
        updateEnvironment(cell.getEnvironment());
        // TODO move this to PhotoRing updateCell method in another phase or something
        cell.photosynthesize();
    }

    // TODO the new EnvironmentalInfluence API
    public void updateEnvironment(CellEnvironment environment) {
        environment.setLightIntensity(calcLightIntensity(environment.getCenterY()));
    }

    double calcLightIntensity(double y) {
        final double transmissionFactor = (depthLimit + y) / depthLimit;
        double tf;
        if (y >= 0) {
            tf = 1.0;
        } else {
            tf = transmissionFactor;
        }
        return maxIntensity.getValue() * tf;
    }
}
