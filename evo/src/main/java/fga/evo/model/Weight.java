package fga.evo.model;

/**
 * The effects of gravity, including cell weight and buoyancy.
 */
public class Weight extends EnvironmentalInfluence {
    private static double gravity = 0.1; // acceleration
    private static double fluidDensity = 0.01; // mass per area

    @Override
    public void addForcesToCell(Cell cell) {
        double cellWeight = cell.getMass() * gravity;
        double displacedFluidWeight = fluidDensity * getDisplacement(cell) * gravity;
        cell.addForce(0, displacedFluidWeight - cellWeight);
    }

    private double getDisplacement(Cell cell) {
        if (cell.getCenterY() <= -cell.getRadius()) {
            return cell.getArea();
        } else if (cell.getCenterY() > cell.getRadius()) {
            return 0;
        } else {
            double fractionSubmerged = (cell.getRadius() - cell.getCenterY()) / (2 * cell.getRadius());
            // linear approximation of the real, hairy equation
            return fractionSubmerged * cell.getArea();
        }
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
