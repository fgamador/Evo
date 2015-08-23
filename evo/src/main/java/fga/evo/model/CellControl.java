package fga.evo.model;

/**
 * @author Franz Amador
 */
public interface CellControl {
    // TODO rename: requestChanges? makeRequests?
    void allocateEnergy(ControlApi cell);

    interface ControlApi {
        void addForce(double x, double y); // TODO hack

        double getCenterY();
        double getEnergy();
        double getFloatArea();
        double getPhotoArea();
        void requestFloatAreaResize(double deltaArea);
        void requestPhotoAreaResize(double deltaArea);
    }
}
