package fga.evo.model;

/**
 * A simple, hard-coded control that seeks a specified depth by adjusting the size of the float ring.
 */
public class FixedDepthSeekingControl implements CellControl {
    private double depth;

    public FixedDepthSeekingControl(double depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Depth may not be less than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void allocateEnergy(CellApi cell) {
        double desiredVelocityY = -(depth + cell.getCenterY()) / 100;
        double desiredDeltaVY = desiredVelocityY - cell.getVelocityY();
        cell.requestFloatAreaResize(desiredDeltaVY / 10);
    }
}
