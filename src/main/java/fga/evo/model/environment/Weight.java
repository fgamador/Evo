package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.util.DoubleParameter;

/**
 * The effects of gravity, including cell weight and buoyancy.
 */
public class Weight implements EnvironmentalInfluence, ForceInfluence {
    public static DoubleParameter gravity = new DoubleParameter(0.5); // acceleration
    public static DoubleParameter fluidDensity = new DoubleParameter(0.1); // mass per area

    @Override
    public void addForce(Cell cell) {
        updateEnvironment(cell);
    }

    @Override
    public void updateEnvironment(Cell cell) {
        double displacedFluidMass = fluidDensity.getValue() * getDisplacement(cell);
        double forceY = (displacedFluidMass - cell.getMass()) * gravity.getValue();
        cell.addForce(0, forceY);
    }

    private double getDisplacement(Cell cell) {
        if (cell.getCenterY() <= -cell.getRadius()) {
            return cell.getArea();
        } else if (cell.getCenterY() > cell.getRadius()) {
            return 0;
        } else {
            double fractionSubmerged = (cell.getRadius() - cell.getCenterY()) / (2 * cell.getRadius());
            // linear approximation of the real, hairy equation; treats ball as a square, not a circle
            return fractionSubmerged * cell.getArea();
        }
    }
}
