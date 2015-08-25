package fga.evo.model;

/**
 * @author Franz Amador
 */
public interface CellControl {
    // TODO rename: requestChanges? makeRequests?
    void allocateEnergy(ControlApi cell);

    interface ControlApi {
        double getRadius();
        double getArea();
        // No getCenterX(). No way for cell to sense this.
        double getCenterY();
        double getVelocityX();
        double getVelocityY();
        double getEnergy();

        double getFloatArea();
        double getPhotoArea();

        void requestFloatAreaResize(double area);
        void requestPhotoAreaResize(double area);
    }
}
