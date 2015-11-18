package fga.evo.model;

class ParentChildControl implements CellControl {
    private double donation;

    public ParentChildControl(double donation) {
        this.donation = donation;
    }

    public final void setDonation(double val) {
        donation = val;
    }

    @Override
    public void allocateEnergy(CellApi cell) {
        if (cell.getRadius() > 5) {
            // run by parent
            cell.requestChildDonation(donation);
        } else {
            // run by child
            cell.requestPhotoAreaResize(cell.getEnergy());
        }
    }
}
