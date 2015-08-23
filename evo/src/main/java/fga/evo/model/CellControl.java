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
        //double getCenterX(); TODO how would it sense this?
        double getCenterY();
        double getVelocityX();
        double getVelocityY();
        double getEnergy();

        double getFloatArea();
        double getPhotoArea();

        void requestFloatAreaResize(double deltaArea);
        void requestPhotoAreaResize(double deltaArea);
    }
}
