package fga.evo.model.control;

public class ParentChildControl implements CellControl {
    private double spawnOdds;
    private double donation;
    private double releaseChildOdds;
    private double releaseParentOdds;

    public ParentChildControl(double spawnOdds, double donation) {
        this.spawnOdds = spawnOdds;
        this.donation = donation;
    }

    public ParentChildControl setSpawnOdds(double val) {
        spawnOdds = val;
        return this;
    }

    public ParentChildControl setDonation(double val) {
        donation = val;
        return this;
    }

    public ParentChildControl setReleaseChildOdds(double val) {
        releaseChildOdds = val;
        return this;
    }

    public ParentChildControl setReleaseParentOdds(double val) {
        releaseParentOdds = val;
        return this;
    }

    @Override
    public void exertControl(CellApi cell) {
        if (cell.getRadius() > 5) {
            // run by parent
            cell.setSpawnOdds(spawnOdds);
            cell.requestChildDonation(donation);
            cell.setReleaseChildOdds(releaseChildOdds);
        } else {
            // run by child
            cell.requestPhotoAreaResize(cell.getEnergy());
            cell.setReleaseParentOdds(releaseParentOdds);
        }
    }
}
