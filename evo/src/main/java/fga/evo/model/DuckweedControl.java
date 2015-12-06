package fga.evo.model;

/**
 * A hard-coded control that emulates duckweed, which floats on the surface and reproduces until it fills the surface layer.
 */
public class DuckweedControl extends FixedDepthSeekingControl {
    public DuckweedControl() {
        super(0);
    }

    @Override
    public void allocateEnergy(CellApi cell) {
        super.allocateEnergy(cell);

        double desiredPhotoArea = 300 * Math.PI;
        double desiredDeltaPhotoArea = desiredPhotoArea - cell.getPhotoArea();
        cell.requestPhotoAreaResize(desiredDeltaPhotoArea / 50);

        // TODO how to detach child? how to stop when layer is full?
        cell.requestChildDonation(5 - desiredDeltaPhotoArea);
    }
}
