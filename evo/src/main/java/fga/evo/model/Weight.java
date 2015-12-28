package fga.evo.model;

/**
 * The effects of gravity, including cell weight and buoyancy.
 */
public class Weight extends EnvironmentalInfluence {
    static DoubleParameter gravity = new DoubleParameter(0.1).register("Weight.gravity"); // acceleration // 0.5
    static DoubleParameter fluidDensity = new DoubleParameter(0.01).register("Weight.fluidDensity"); // mass per area // 0.1

    @Override
    public void addForcesToCell(Cell cell) {
        double cellWeight = cell.getMass() * gravity.getValue();
        double displacedFluidWeight = fluidDensity.getValue() * getDisplacement(cell) * gravity.getValue();
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
}
