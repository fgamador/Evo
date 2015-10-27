package fga.evo.model;

/**
 * Created by Franz on 10/25/2015.
 */
class ParentChildControl implements CellControl {
    private double donation;

    public ParentChildControl(double donation) {
        this.donation = donation;
    }

    public final void setDonation(double val) {
        donation = val;
    }

    @Override
    public void allocateEnergy(ControlApi cell) {
        if (cell.getRadius() > 5) {
            // run by parent
            cell.requestChildDonation(donation);
        } else {
            // run by child
            cell.requestPhotoAreaResize(cell.getEnergy());
        }
    }
}
