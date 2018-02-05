package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.util.DoubleParameter;

/**
 * The light illuminating the cells.
 */
public class Illumination extends EnvironmentalInfluence {
    public static DoubleParameter maxIntensity = new DoubleParameter(1);

    private double depth;

    public Illumination(double depth) {
        setDepth(depth);
    }

    public void setDepth(double depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void addEnergyToCell(Cell cell) {
        double lightIntensity = calcLightIntensity(cell.getCenterY());
        cell.photosynthesize(lightIntensity);
    }

    double calcLightIntensity(double y) {
        return (y >= 0) ? maxIntensity.getValue() : maxIntensity.getValue() * (depth + y) / depth;
    }
}
