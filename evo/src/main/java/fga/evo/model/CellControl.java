package fga.evo.model;

/**
 * @author Franz Amador
 */
public interface CellControl {
    void allocateEnergy(CellControl.Cell cell);

    /**
     * The API that a CellControl uses to control a Cell.
     */
    interface Cell {
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
    }
}
