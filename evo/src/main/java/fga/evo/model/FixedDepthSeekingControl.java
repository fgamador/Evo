package fga.evo.model;

/**
 * A simple, hard-coded control that seeks a specified depth by adjusting the size of the float ring.
 */
public class FixedDepthSeekingControl implements CellControl {
    private double targetDepth;

    public FixedDepthSeekingControl(double targetDepth) {
        if (targetDepth < 0) {
            throw new IllegalArgumentException("Depth may not be less than zero but is " + targetDepth);
        }

        this.targetDepth = targetDepth;
    }

    @Override
    public void exertControl(CellApi cell) {
        double desiredVelocityY = -(targetDepth + cell.getCenterY()) / 100;
        double desiredDeltaVY = desiredVelocityY - cell.getVelocityY();
        cell.requestFloatAreaResize(desiredDeltaVY / 10);
    }
}
