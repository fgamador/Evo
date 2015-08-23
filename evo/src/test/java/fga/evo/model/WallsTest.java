package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class WallsTest {
    private Walls walls;
    private Cell cell;

    @Before
    public void setUp() {
        walls = new Walls(10, 10);
        cell = new Cell(1);
    }

    @Test
    public void testAddCollisionForcesToCell_JustTouching() {
        cell.setPosition(1, -1);

        walls.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_LowXYCollision() {
        cell.setPosition(0.5, -0.5);

        walls.addForcesToCell(cell);

        assertNetForce(0.5, -0.5, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_HighXYCollision() {
        cell.setPosition(9.5, -9.5);

        walls.addForcesToCell(cell);

        assertNetForce(-0.5, 0.5, cell);
    }
}
