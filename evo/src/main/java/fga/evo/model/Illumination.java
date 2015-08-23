package fga.evo.model;

/**
 * The light illuminating the cells.
 *
 * @author Franz Amador
 */
public class Illumination extends EnvironmentalInfluence {
    private static double MAX_INTENSITY = 2;

    private double depth;

    public Illumination(final double depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void addEnergyToCell(final Cell cell) {
        cell.photosynthesize(calcLightIntensity(cell.getCenterY()));
    }

    public final double calcLightIntensity(double y) {
        assert y <= 0 : y;
        return MAX_INTENSITY * (depth + y) / depth;
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
