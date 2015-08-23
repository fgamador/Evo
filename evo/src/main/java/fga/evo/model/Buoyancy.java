package fga.evo.model;

/**
 * Buoyancy of the cells in their fluid medium.
 *
 * @author Franz Amador
 */
public class Buoyancy extends EnvironmentalInfluence {
    private static double gravity = 0.1; // acceleration
    private static double fluidDensity = 0.01; // mass per area

    @Override
    public void addForcesToCell(Cell cell) {
        double cellWeight = cell.getMass() * gravity;
        double displacedFluidWeight = fluidDensity * cell.getArea() * gravity;
        cell.addForce(0, displacedFluidWeight - cellWeight);
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getGravity() {
        return gravity;
    }

    public static void setGravity(double val) {
        gravity = val;
    }

    public static double getFluidDensity() {
        return fluidDensity;
    }

    public static void setFluidDensity(double val) {
        fluidDensity = val;
    }
}
