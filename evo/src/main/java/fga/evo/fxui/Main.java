package fga.evo.fxui;

import fga.evo.model.*;

public class Main extends Evo {
    @Override
    protected void addInfluences(World world) {
        world.addEnvironmentalInfluence(new SurroundingWalls(0, WIDTH, -WATER_DEPTH, AIR_HEIGHT));
        world.addEnvironmentalInfluence(new Drag());
        world.addEnvironmentalInfluence(new Weight());
        world.addEnvironmentalInfluence(new Illumination(WATER_DEPTH));
    }

    @Override
    protected void populate(World world) {
//        Cell cell = new Cell(10, new FixedDepthSeekingControl(0));
        Cell cell = createCell();
        cell.setCenterPosition(WIDTH / 2, -WATER_DEPTH / 2);
        world.addCell(cell);

//        for (int i = 0; i < 10; i++) {
//            Cell cell = new Cell(1, new DuckweedControl());
//            world.addCell(cell);
//            cell.setCenterPosition(50 + 50*i, -200 - 30*i);
//        }
//
//        for (int i = 9; i >= 0; i--) {
//            Cell cell = new Cell(1, new DuckweedControl());
//            world.addCell(cell);
//            cell.setCenterPosition(950 - 50*i, -200 - 30*i);
//        }
    }

    @Override
    protected Cell createCell() {
        return new Cell(10, new DuckweedControl());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
