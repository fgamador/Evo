package fga.evo.model;

/**
 * A simple, hard-coded control that seeks a specified depth (y) by adjusting the size of the float ring.
 *
 * @author Franz Amador
 */
public class FixedDepthSeekingControl implements CellControl {
    private double y;

    public FixedDepthSeekingControl(double y) {
        this.y = y;
    }

    @Override
    public void allocateEnergy(Cell cell) {
        cell.addForce(0, 0.01 * (y - cell.getCenterY()));
    }
}
