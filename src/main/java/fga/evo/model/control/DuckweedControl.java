package fga.evo.model.control;

/**
 * A hard-coded control that emulates duckweed, which floats on the surface and reproduces until it fills the surface layer.
 */
public class DuckweedControl extends FixedDepthSeekingControl {
    public DuckweedControl() {
        super(0);
    }

    @Override
    public void exertControl(CellApi cell) {
        super.exertControl(cell);

        double desiredPhotoArea = 300 * Math.PI;
        double desiredDeltaPhotoArea = desiredPhotoArea - cell.getPhotoArea();
        cell.requestPhotoAreaResize_Old(desiredDeltaPhotoArea / 50);

        double areaFactor = 1 + cell.getPhotoArea() - desiredDeltaPhotoArea;
        double overlapFactor = 0.5 - cell.getRecentTotalOverlap();
        cell.setSpawnOdds(areaFactor * overlapFactor);

        cell.requestChildDonation(5 - desiredDeltaPhotoArea);
        cell.setReleaseParentOdds(cell.getNonFloatArea() - 200 * Math.PI);
    }
}
