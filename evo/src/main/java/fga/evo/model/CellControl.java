package fga.evo.model;

/**
 * The brains and eventually the genome of a cell.
 */
public interface CellControl {
    void allocateEnergy(CellApi cell);

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

        void requestFloatAreaResize(double growthEnergy);

        void requestPhotoAreaResize(double growthEnergy);

        void requestChildDonation(double donatedEnergy);

        void setSpawnOdds(double odds);

        //void setReleaseChildOdds(double odds);

        //void setReleaseParentOdds(double odds);
    }
}
