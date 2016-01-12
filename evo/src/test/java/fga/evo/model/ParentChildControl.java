package fga.evo.model;

class ParentChildControl implements CellControl {
    private double spawnOdds;
    private double donation;
    private double releaseChildOdds;
    private double releaseParentOdds;

    public ParentChildControl(double spawnOdds, double donation) {
        this.spawnOdds = spawnOdds;
        this.donation = donation;
    }

    public void setSpawnOdds(double val) {
        spawnOdds = val;
    }

    public void setDonation(double val) {
        donation = val;
    }

    public void setReleaseChildOdds(double val) {
        releaseChildOdds = val;
    }

    public void setReleaseParentOdds(double val) {
        releaseParentOdds = val;
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
