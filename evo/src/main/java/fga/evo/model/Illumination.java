package fga.evo.model;

/**
 * The light illuminating the cells.
 */
public class Illumination extends EnvironmentalInfluence {
    private static double MAX_INTENSITY = 1;

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
        return (y >= 0) ? MAX_INTENSITY : MAX_INTENSITY * (depth + y) / depth;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getMaxIntensity() {
        return MAX_INTENSITY;
    }

    public static void setMaxIntensity(final double val) {
        MAX_INTENSITY = val;
    }
}
