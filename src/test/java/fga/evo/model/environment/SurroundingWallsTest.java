package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import fga.evo.model.physics.NewtonianBody;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class SurroundingWallsTest extends EvoTest {
    private SurroundingWalls walls;
    private Cell cell;

    @Before
    public void setUp() {
        walls = new SurroundingWalls(0, 10, -10, 0);
        cell = new Cell(1);
    }

    @Test
    public void justTouchingAddsNoForce() {
        cell.setCenterPosition(1, -1);
        CellEnvironment environment = cell.getEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void lowXYCollisionAddsForce() {
        cell.setCenterPosition(0.5, -0.5);
        CellEnvironment environment = cell.getEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0.5, -0.5, environment);
    }

    @Test
    public void highXYCollisionAddsForce() {
        cell.setCenterPosition(9.5, -9.5);
        CellEnvironment environment = cell.getEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(-0.5, 0.5, environment);
    }

    @Test
    public void highCeilingAddsNoForce() {
        walls = new SurroundingWalls(-5, 5, -5, 5);
        cell.setCenterPosition(0, 0);
        CellEnvironment environment = cell.getEnvironment();

        walls.updateEnvironment(environment, cell);

        assertNetForce(0, 0, environment);
    }
}
