package fga.evo.model;

public class CellCellCollision extends CellCellInteraction {
    CellCellCollision(Cell cell1, Cell cell2) {
        super(cell1, cell2);
    }

    @Override
    protected double calculateForce() {
        return Cell.COLLISION_SPRING_CONSTANT * overlap;
    }
}
