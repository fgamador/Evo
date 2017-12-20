package fga.evo.model;

import fga.evo.model.physics.Ball;

/**
 * The effects of gravity, including cell weight and buoyancy.
 */
public class Weight extends EnvironmentalInfluence {
    static DoubleParameter gravity = new DoubleParameter(0.5); // acceleration
    static DoubleParameter fluidDensity = new DoubleParameter(0.1); // mass per area // TODO 0.1 (0.01)

    @Override
    public void addForcesToCell(Ball cell) {
        double cellWeight = cell.getMass() * gravity.getValue();
        double displacedFluidWeight = fluidDensity.getValue() * getDisplacement(cell) * gravity.getValue();
        cell.addForce(0, displacedFluidWeight - cellWeight);
    }

    private double getDisplacement(Ball cell) {
        if (cell.getCenterY() <= -cell.getRadius()) {
            return cell.getArea();
        } else if (cell.getCenterY() > cell.getRadius()) {
            return 0;
        } else {
            double fractionSubmerged = (cell.getRadius() - cell.getCenterY()) / (2 * cell.getRadius());
            // linear approximation of the real, hairy equation; treats cell as a square, not a circle
            return fractionSubmerged * cell.getArea();
        }
    }
}
