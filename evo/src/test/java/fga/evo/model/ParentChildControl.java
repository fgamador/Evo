package fga.evo.model;

class ParentChildControl implements CellControl {
    private double spawnOdds;
    private double donation;
    private double releaseOdds;

    public ParentChildControl(double spawnOdds, double donation) {
        this(spawnOdds, donation, 0);
    }

    public ParentChildControl(double spawnOdds, double donation, double releaseOdds) {
        this.spawnOdds = spawnOdds;
        this.donation = donation;
        this.releaseOdds = releaseOdds;
    }

    public void setSpawnOdds(double val) {
        spawnOdds = val;
    }

    public void setDonation(double val) {
        donation = val;
    }

    public void setReleaseOdds(double val) {
        releaseOdds = val;
    }

    @Override
    public void exertControl(CellApi cell) {
        if (cell.getRadius() > 5) {
            // run by parent
            cell.setSpawnOdds(spawnOdds);
            cell.requestChildDonation(donation);
            cell.setReleaseChildOdds(releaseOdds);
        } else {
            // run by child
            cell.requestPhotoAreaResize(cell.getEnergy());
        }
    }
}
