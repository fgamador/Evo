package fga.evo.model;

import fga.evo.model.control.DuckweedControl;

import java.util.Collection;

public class TimingMain {
    private static final int WIDTH = 1000;
    private static final int AIR_HEIGHT = 50;
    private static final int WATER_DEPTH = 500;
    private static final long TICKS = 100000;

    public static void main(String[] args) {
        runWorld(createWorld(), 1000);

        long start = System.currentTimeMillis();
        runWorld(createWorld(), TICKS);
        long end = System.currentTimeMillis();

        System.out.println(TICKS + " ticks in " + (end - start) / 1000.0 + "s");
        System.out.println((end - start) * 1.0 / TICKS + " millis/tick");
    }

    private static void runWorld(World world, long ticks) {
        for (long i = 0; i < ticks; ++i)
            world.tick();
    }

    private static World createWorld() {
        World world = new World();
        addInfluences(world);
        populate(world);
        return world;
    }

    private static void addInfluences(World world) {
        world.addEnvironmentalInfluence(new SurroundingWalls(0, WIDTH, -WATER_DEPTH, AIR_HEIGHT));
        world.addEnvironmentalInfluence(new Drag());
        world.addEnvironmentalInfluence(new Weight());
        world.addEnvironmentalInfluence(new Illumination(WATER_DEPTH));
    }

    private static void populate(World world) {
        Cell cell = createCell();
        cell.setCenterPosition(200, -100);
        world.addCell(cell);
    }

    private static Cell createCell() {
        return new Cell(10, new DuckweedControl());
    }

    private static void tick(World world) {
        Collection<Cell> newCells = world.tick();
    }
}
