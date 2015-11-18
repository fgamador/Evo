package fga.evo.model;

/**
 * A simple, hard-coded control that seeks a specified depth by adjusting the size of the float ring.
 *
 * @author Franz Amador
 */
public class FixedDepthSeekingControl implements CellControl {
    private double depth;

    public FixedDepthSeekingControl(double depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void allocateEnergy(CellApi cell) {
        final double desiredVelocityY = -(depth + cell.getCenterY()) / 100;
        final double desiredDeltaVY = desiredVelocityY - cell.getVelocityY();
        cell.requestFloatAreaResize(desiredDeltaVY / 10);

//        double depthBuoyancyDelta = Math.min(-(depth + cell.getCenterY()), 5);
//        double velocityBuoyancyDelta = -cell.getVelocityY();
//        cell.requestFloatAreaResize(depthBuoyancyDelta + 10 * velocityBuoyancyDelta);

//        cell.requestFloatAreaResize(-(depth + cell.getCenterY()));
    }
}
