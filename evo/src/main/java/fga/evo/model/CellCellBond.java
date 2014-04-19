package fga.evo.model;

public class CellCellBond extends CellCellInteraction {
    private double lastTickSeparation;

    CellCellBond(Cell cell1, Cell cell2) {
        super(cell1, cell2);
        lastTickSeparation = separation;
    }

    @Override
    protected double calculateForce() {
        double separationVelocity = separation - lastTickSeparation;
        lastTickSeparation = separation;
        double dampingForce = -Cell.BOND_DAMPING_CONSTANT * separationVelocity * separationVelocity;
        double springForce = Cell.BOND_SPRING_CONSTANT * overlap;
        return springForce + dampingForce;
    }
}
