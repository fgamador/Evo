package fga.evo.model.biology;

public class CellPairShadowing {
    public static void addShadowingToLowerCell(Cell cell1, Cell cell2) {
        if (cell1.getCenterY() > cell2.getCenterY())
            addShadowingInner(cell1, cell2);
        else
            addShadowingInner(cell2, cell1);
    }

    private static void addShadowingInner(Cell upperCell, Cell lowerCell) {
        lowerCell.getEnvironment().addShadowing(calcTransmissionFraction(upperCell, lowerCell));
    }

    private static double calcTransmissionFraction(Cell upperCell, Cell lowerCell) {
        double overlapFraction = calcXOverlap(upperCell, lowerCell) / (2 * lowerCell.getRadius());
        return 1 - overlapFraction + overlapFraction * upperCell.getShadowTransmissionFraction();
    }

    private static double calcXOverlap(Cell cell1, Cell cell2) {
        double cell1MinX = cell1.getCenterX() - cell1.getRadius();
        double cell1MaxX = cell1.getCenterX() + cell1.getRadius();
        double cell2MinX = cell2.getCenterX() - cell2.getRadius();
        double cell2MaxX = cell2.getCenterX() + cell2.getRadius();
        double overlapMinX = Math.max(cell1MinX, cell2MinX);
        double overlapMaxX = Math.min(cell1MaxX, cell2MaxX);
        return Math.max(0, overlapMaxX - overlapMinX);
    }
}
