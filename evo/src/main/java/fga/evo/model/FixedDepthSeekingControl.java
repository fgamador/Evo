package fga.evo.model;

/**
 * A simple, hard-coded control that seeks a specified depth by adjusting the size of the float ring.
 *
 * @author Franz Amador
 */
public class FixedDepthSeekingControl implements CellControl {
    private double depth;

    public FixedDepthSeekingControl(double depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than zero but is " + depth);
        }

        this.depth = depth;
    }

    @Override
    public void allocateEnergy(ControlApi cell) {
        double depthBuoyancyDelta = Math.min(-(depth + cell.getCenterY()), 5);
        double velocityBuoyancyDelta = -cell.getVelocityY();
        // TODO need to request absolute area, not delta
        cell.requestFloatAreaResize(depthBuoyancyDelta + 10 * velocityBuoyancyDelta);
    }

    // http://stackoverflow.com/questions/2887815/speeding-up-math-calculations-in-java
    // domain -inf..inf (-5..5 really, maybe -10..10), range 0..1
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
