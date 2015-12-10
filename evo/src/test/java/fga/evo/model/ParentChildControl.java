package fga.evo.model;

class ParentChildControl implements CellControl {
    private double donation;
    private double spawnOdds;

    public ParentChildControl(double spawnOdds, double donation) {
        this.spawnOdds = spawnOdds;
        this.donation = donation;
    }

    public final void setDonation(double val) {
        donation = val;
    }

    public final void setSpawnOdds(double val) {
        spawnOdds = val;
    }

    @Override
    public void allocateEnergy(CellApi cell) {
        if (cell.getRadius() > 5) {
            // run by parent
            cell.setSpawnOdds(spawnOdds);
            cell.requestChildDonation(donation);
        } else {
            // run by child
            cell.requestPhotoAreaResize(cell.getEnergy());
        }
    }
}
