package fga.evo.model.biology;

public class CellPairShadowing {
    public static void addShadowing(Cell cell1, Cell cell2) {
        Cell upperCell, lowerCell;
        if (cell1.getCenterY() > cell2.getCenterY()) {
            upperCell = cell1;
            lowerCell = cell2;
        } else {
            upperCell = cell2;
            lowerCell = cell1;
        }

        double overlapFraction = calcXOverlap(cell1, cell2) / (2 * lowerCell.getRadius());
        lowerCell.getEnvironment().addShadowing(1 - overlapFraction + overlapFraction * upperCell.getShadowTransmissionFraction());
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
