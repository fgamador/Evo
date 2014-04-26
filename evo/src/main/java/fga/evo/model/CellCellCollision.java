package fga.evo.model;

public class CellCellCollision extends CellCellInteraction {
    public static final double SPRING_CONSTANT = 1;

    CellCellCollision(Cell cell1, Cell cell2) {
        super(cell1, cell2);
    }

    @Override
    protected double calculateForce() {
        return SPRING_CONSTANT * overlap;
    }
}
