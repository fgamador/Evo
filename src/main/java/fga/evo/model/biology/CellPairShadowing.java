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

        lowerCell.getEnvironment().addShadowing(upperCell.getShadowTransmissionFraction());
    }
}
