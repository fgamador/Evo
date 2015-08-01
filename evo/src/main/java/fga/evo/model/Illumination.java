package fga.evo.model;

/**
 * The light illuminating the cells.
 *
 * @author Franz Amador
 */
public class Illumination extends EnvironmentalInfluence {
    private static double MAX_INTENSITY = 2;

    private double height;

    public Illumination(final double height) {
        this.height = height;
    }

    @Override
    public void addEnergyToCell(final Cell cell) {
        cell.photosynthesize(calcLightIntensity(cell.getCenterY()));
    }

    public final double calcLightIntensity(double y) {
        return MAX_INTENSITY * (height - y) / height;
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
