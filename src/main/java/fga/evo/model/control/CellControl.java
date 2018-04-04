package fga.evo.model.control;

/**
 * The brains and eventually the genome of a cell.
 */
public interface CellControl {
    void exertControl(CellApi cell);

    /**
     * The API that a CellControl uses to control a Cell.
     */
    interface CellApi {
        double getRadius();

        double getArea();

        // There is no getCenterX() because there is no way for a cell to sense this.

        double getCenterY();

        double getVelocityX();

        double getVelocityY();

        double getEnergy();

        double getFloatArea();

        double getPhotoArea();

        double getNonFloatArea();

        double getRecentTotalOverlap();

        void requestFloatAreaResize(double deltaArea);

        void requestPhotoAreaResize(double deltaArea);

        void requestChildDonation(double donatedEnergy);

        void setSpawnOdds(double odds);

        void setReleaseChildOdds(double odds);

        void setReleaseParentOdds(double odds);
    }
}
