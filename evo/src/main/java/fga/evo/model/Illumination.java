package fga.evo.model;

/**
 * The light illuminating the cells.
 */
public class Illumination extends EnvironmentalInfluence {
    static DoubleParameter maxIntensity = new DoubleParameter(1).register("maxIntensity");

    private double depth;

    public Illumination(double depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void addEnergyToCell(Cell cell) {
        cell.photosynthesize(calcLightIntensity(cell.getCenterY()));
    }

    public double calcLightIntensity(double y) {
        return (y >= 0) ? maxIntensity.getValue() : maxIntensity.getValue() * (depth + y) / depth;
    }
}
