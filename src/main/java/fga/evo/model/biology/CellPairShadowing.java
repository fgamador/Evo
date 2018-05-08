package fga.evo.model.biology;

public class CellPairShadowing {
    public static void addShadowing(Cell cell1, Cell cell2) {
        Cell lowerCell = (cell1.getCenterX() > cell2.getCenterY()) ? cell2 : cell1;
        lowerCell.getEnvironment().addShadowing(0.5);
    }
}
