package fga.evo.model;

public abstract class CellCellInteraction {
    protected final Cell cell1, cell2;
    protected double deltaX, deltaY;
    protected double separation;
    protected double overlap;
    protected double forceX, forceY;

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
        double force = calculateForce();
        forceX = force * deltaX / separation;
        forceY = force * deltaY / separation;
    }

    /** A positive force acts to increase separation. */
    abstract protected double calculateForce();

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
