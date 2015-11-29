package fga.evo.model;

public class CellCellBond extends CellCellInteraction {
    public static double SPRING_CONSTANT = 1;
    public static double DAMPING_CONSTANT = 0.01;

    CellCellBond(Cell cell1, Cell cell2) {
        super(cell1, cell2);
    }

    @Override
    protected double calculateForce() {
        double minRadius = Math.min(cell1.getRadius(), cell2.getRadius());
        double overlapMagnitude = Math.abs(overlap);
        double contactLength = (overlapMagnitude < minRadius) ? overlapMagnitude : minRadius; // linear approximation
        return SPRING_CONSTANT * contactLength * Math.signum(overlap);
    }
}
