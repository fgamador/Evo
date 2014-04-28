package fga.evo.model;

public class CellCellBond extends CellCellInteraction {
    public static double SPRING_CONSTANT = 1;
    public static double DAMPING_CONSTANT = 0.01;

    private double lastTickSeparation;

    CellCellBond(Cell cell1, Cell cell2) {
        super(cell1, cell2);
        lastTickSeparation = separation;
    }

    @Override
    protected double calculateForce() {
        double separationVelocity = separation - lastTickSeparation;
        lastTickSeparation = separation;
        double dampingForce = -Math.signum(separationVelocity) * DAMPING_CONSTANT * separationVelocity
            * separationVelocity;
        double springForce = SPRING_CONSTANT * overlap;
        return springForce + dampingForce;
    }
}
