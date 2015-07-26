package fga.evo.model;

// TODO factor out common base class with Box and Fluid: EnvironmentComponent or something,
// with methods for the various tick phases (with default do-nothing implementations)
/**
 * The light illuminating the cells.
 *
 * @author Franz Amador
 */
public class LightField {
    private static double MAX_INTENSITY = 2;

    private double height;

    public LightField(final double height) {
        this.height = height;
    }

    public final void illuminateCell(final Cell cell) {
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
