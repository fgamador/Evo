package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class SurroundingWallsTest extends EvoTest {
    @Test
    public void justTouchingAddsNoForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(1, -1);
        CellEnvironment environment = new CellEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0, 0, cell.getEnvironment());
    }

    @Test
    public void lowXYCollisionAddsExpectedForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0.5, -0.5);
        CellEnvironment environment = new CellEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0.5, -0.5, cell.getEnvironment());
    }

    @Test
    public void highXYCollisionAddsExpectedForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(9.5, -9.5);
        CellEnvironment environment = new CellEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(-0.5, 0.5, cell.getEnvironment());
    }

    @Test
    public void highCeilingAddsNoForce() {
        SurroundingWalls walls = new SurroundingWalls(-5, 5, -5, 5);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0, 0);
        CellEnvironment environment = new CellEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0, 0, cell.getEnvironment());
    }
}
