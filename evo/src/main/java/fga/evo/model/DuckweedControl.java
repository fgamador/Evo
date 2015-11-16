package fga.evo.model;

/**
 * A hard-coded control that emulates duckweed, which floats on the surface and reproduces until it fills the surface layer.
 *
 * @author Franz Amador
 */
public class DuckweedControl extends FixedDepthSeekingControl {
    public DuckweedControl() {
        super(0);
    }

    @Override
    public void allocateEnergy(CellControl.Cell cell) {
        super.allocateEnergy(cell);
        // grow photo area up to TODO
        final double desiredPhotoArea = 500 * Math.PI;
        final double desiredDeltaPhotoArea = desiredPhotoArea - cell.getPhotoArea();
        cell.requestPhotoAreaResize(desiredDeltaPhotoArea / 10);
        // TODO how to detach child? how to stop when layer is full?
        // cell.requestChildDonation(cell.getArea() - 90 * Math.PI);
    }
}
