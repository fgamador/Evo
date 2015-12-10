package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class SurroundingWallsTest {
    private SurroundingWalls walls;
    private Cell cell;

    @Before
    public void setUp() {
        walls = new SurroundingWalls(0, 10, -10, 0);
        cell = new Cell(1);
    }

    @Test
    public void testAddCollisionForcesToCell_JustTouching() {
        cell.setCenterPosition(1, -1);

        walls.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_LowXYCollision() {
        cell.setCenterPosition(0.5, -0.5);

        walls.addForcesToCell(cell);

        assertNetForce(0.5, -0.5, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_HighXYCollision() {
        cell.setCenterPosition(9.5, -9.5);

        walls.addForcesToCell(cell);

        assertNetForce(-0.5, 0.5, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_HighCeiling() {
        walls = new SurroundingWalls(-5, 5, -5, 5);
        cell.setCenterPosition(0, 0);

        walls.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }
}