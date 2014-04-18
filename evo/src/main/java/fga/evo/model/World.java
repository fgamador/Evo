package fga.evo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class World {
    static final double FLUID_RESISTANCE = 0.001;
    static final double LIGHT_INTENSITY = 2;

    private Random random = new Random();
    private int width, height;
    private List<Cell> cells = new ArrayList<Cell>();

    // -- initialization --

    public World(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void populate(int numCells, double minRadius, double maxRadius) {
        double deltaRadius = maxRadius - minRadius;
        for (int i = 0; i < numCells; i++) {
            double radius = minRadius + random.nextDouble() * deltaRadius;
            int ceilRadius = (int) Math.ceil(radius);
            for (;;) {
                double centerX = ceilRadius + random.nextInt(width - (2 * ceilRadius));
                double centerY = ceilRadius + random.nextInt(height - (2 * ceilRadius));
                Cell cell = new Cell(this, newThruster(), radius, centerX, centerY);
                if (!overlapsExistingCell(cell)) {
                    cells.add(cell);
                    break;
                }
            }
        }
    }

    private boolean overlapsExistingCell(Cell cell1) {
        // TODO find a non-O(N^2) way
        for (int i = 0; i < cells.size(); i++) {
            Cell cell2 = cells.get(i);
            CellCellInteraction pair = new CellCellInteraction(cell1, cell2);
            if (pair.getOverlap() > 0)
                return true;
        }
        return false;
    }

    private Thruster newThruster() {
        double force = 0.1 + (random.nextDouble() * 0.1);
        double angle = randomAngle();
        double forceX = force * Math.cos(angle);
        double forceY = force * Math.sin(angle);
        return new PulseThruster(forceX, forceY, 10 + random.nextInt(5), 100 + random.nextInt(100));
    }

    // -- simulation --

    /** Advances the model one time tick. */
    public Set<Cell> tick() {
        for (Cell cell : cells) {
            cell.pretick();
        }

        detectInteractions();

        Set<Cell> newCells = new HashSet<>();
        for (Cell cell : cells) {
            cell.balanceEnergy(newCells);
        }
        cells.addAll(newCells);

        for (Cell cell : cells) {
            cell.calculateForces();
        }
        for (Cell cell : cells) {
            cell.move();
        }

        return newCells;
    }

    /** Finds all the pairs of interacting cells. */
    private void detectInteractions() {
        // TODO find a non-O(N^2) way
        for (int i = 0; i < cells.size(); i++) {
            Cell cell1 = cells.get(i);
            for (int j = i + 1; j < cells.size(); j++) {
                Cell cell2 = cells.get(j);
                CellCellInteraction pair = new CellCellInteraction(cell1, cell2);
                if (pair.getOverlap() > 0 || cell2.equals(cell1.getChild())) {
                    pair.calculateForces();
                    cell1.addInteracting(pair);
                    cell2.addInteracting(pair);
                }
            }
        }
    }

    /** Returns the light intensity at a given depth. */
    final double getLightIntensity(double y) {
        return LIGHT_INTENSITY * (height - y) / height;
    }

    /** Returns the horizontal strength of the current at a specified point. */
    final double getCurrentX(double x, double y) {
        return Math.sin(2 * Math.PI * y / height) * Math.sin(Math.PI * x / width);
    }

    /** Returns the vertical strength of the current at a specified point. */
    final double getCurrentY(double x, double y) {
        return -Math.sin(2 * Math.PI * x / width) * Math.sin(Math.PI * y / height);
    }

    public final List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public final double getWidth() {
        return width;
    }

    public final double getHeight() {
        return height;
    }

    final double randomAngle() {
        return random.nextDouble() * 2 * Math.PI;
    }
}
