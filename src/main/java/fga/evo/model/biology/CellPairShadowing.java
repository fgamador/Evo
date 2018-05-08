package fga.evo.model.biology;

public class CellPairShadowing {
    public static void addShadowing(Cell cell1, Cell cell2) {
        cell2.getEnvironment().addShadowing(0.1);
    }
}
