package fga.evo.model;

import fga.evo.model.control.DuckweedControl;
import fga.evo.model.environment.Drag;
import fga.evo.model.environment.Illumination;
import fga.evo.model.environment.SurroundingWalls;
import fga.evo.model.environment.Weight;

// With original intersection detection and width 1000
//   100000 ticks in 1.785s
//   0.01785 millis/tick

public class TimingMain {
    private static final int WIDTH = 10000;
    private static final int AIR_HEIGHT = 50;
    private static final int WATER_DEPTH = 500;
    private static final long TICKS = 10000000;

    public static void main(String[] args) {
        runWorld(createWorld(), 1000);

        long start = System.currentTimeMillis();
        runWorld(createWorld(), TICKS);
        long end = System.currentTimeMillis();

        double millis = end - start;
        System.out.println(TICKS + " ticks in " + millis / 1000 + "s");
        System.out.println(millis / TICKS + " millis/tick");
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
        for (int centerX = 200; centerX < WIDTH; centerX += 200)
            addCell(world, centerX, -50);
    }

    private static void addCell(World world, int centerX, int centerY) {
        Cell cell = createCell();
        cell.setCenterPosition(centerX, centerY);
        world.addCell(cell);
    }

    private static Cell createCell() {
        return new Cell(10, new DuckweedControl());
    }
}
