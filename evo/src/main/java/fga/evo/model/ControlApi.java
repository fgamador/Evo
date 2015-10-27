package fga.evo.model;

/**
 * The API that a CellControl uses to control a Cell.
 */
public interface ControlApi {
    double getRadius();

    double getArea();

    // No getCenterX(). No way for cell to sense this.
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
