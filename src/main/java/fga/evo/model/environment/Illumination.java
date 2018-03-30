package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.util.DoubleParameter;

/**
 * The light illuminating the cells.
 */
public final class Illumination extends EnvironmentalInfluence {
    public static DoubleParameter maxIntensity = new DoubleParameter(1);

    private double depthLimit;

    public Illumination(double depthLimit) {
        setDepthLimit(depthLimit);
    }

    public void setDepthLimit(double value) {
        if (!(value > 0))
            throw new IllegalArgumentException("Depth must be greater than zero but is " + value);

        depthLimit = value;
    }

    @Override
    public void updateEnvironment(Cell cell) {
        cell.getEnvironment().setLightIntensity(calcLightIntensity(cell.getCenterY()));
    }

    double calcLightIntensity(double y) {
        return maxIntensity.getValue() * calcTransmissionFactor(y);
    }

    double calcTransmissionFactor(double y) {
        if (y >= 0)
            return 1;
        else if (y <= -depthLimit)
            return 0;
        else
            return 1 + y / depthLimit;
    }
}
