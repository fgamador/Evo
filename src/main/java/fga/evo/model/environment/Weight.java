package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.physics.Ball;
import fga.evo.model.util.DoubleParameter;

/**
 * The effects of gravity, including cell weight and buoyancy.
 */
public class Weight extends EnvironmentalInfluence {
    public static DoubleParameter gravity = new DoubleParameter(0.5); // acceleration
    public static DoubleParameter fluidDensity = new DoubleParameter(0.1); // mass per area // TODO 0.1 (0.01)

    @Override
    public void updateEnvironment(CellEnvironment environment, Cell cell) {
        double cellWeight = cell.getMass() * gravity.getValue();
        double displacedFluidWeight = fluidDensity.getValue() * getDisplacement(cell) * gravity.getValue();
        cell.getEnvironment().addForce((double) 0, displacedFluidWeight - cellWeight);
    }

    private double getDisplacement(Ball ball) {
        if (ball.getCenterY() <= -ball.getRadius()) {
            return ball.getArea();
        } else if (ball.getCenterY() > ball.getRadius()) {
            return 0;
        } else {
            double fractionSubmerged = (ball.getRadius() - ball.getCenterY()) / (2 * ball.getRadius());
            // linear approximation of the real, hairy equation; treats ball as a square, not a circle
            return fractionSubmerged * ball.getArea();
        }
    }
}
