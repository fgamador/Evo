package fga.evo.model;

public final class CellCellInteraction {
    private final Cell cell1, cell2;
    private double deltaX, deltaY;
    private double separation;
    private double overlap;
    private double forceX, forceY;

    CellCellInteraction(final Cell cell1, final Cell cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        calculatePositions();
    }

    private void calculatePositions() {
        deltaX = cell2.getCenterX() - cell1.getCenterX();
        deltaY = cell2.getCenterY() - cell1.getCenterY();
        separation = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        overlap = cell1.getRadius() + cell2.getRadius() - separation;
    }

    void calculateForces() {
        double force = Cell.INTERACTION_SPRING_CONSTANT * overlap;
        forceX = force * deltaX / separation;
        forceY = force * deltaY / separation;
    }

    public final Cell getCell1() {
        return cell1;
    }

    public final Cell getCell2() {
        return cell2;
    }

    public final double getDeltaX() {
        return deltaX;
    }

    public final double getDeltaY() {
        return deltaY;
    }

    public final double getSeparation() {
        return separation;
    }

    public final double getOverlap() {
        return overlap;
    }

    public final double getForceX() {
        return forceX;
    }

    public final double getForceY() {
        return forceY;
    }
}
