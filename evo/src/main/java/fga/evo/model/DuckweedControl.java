package fga.evo.model;

/**
 * A hard-coded control that emulates duckweed, which floats on the surface and reproduces until it fills the surface layer.
 *
 * @author Franz Amador
 */
public class DuckweedControl implements CellControl {
    @Override
    public void allocateEnergy(ControlApi cell) {
        // seek the surface
        cell.requestFloatAreaResize(-cell.getCenterY());
        cell.requestPhotoAreaResize(100 * Math.PI);
        // TODO how to detach child? how to stop when layer is full?
        // cell.requestChildDonation(cell.getArea() - 90 * Math.PI);
    }
}
