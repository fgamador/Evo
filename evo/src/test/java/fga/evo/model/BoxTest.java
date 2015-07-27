package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;

public class BoxTest {
    private Box box;
    private Cell cell;

    @Before
    public void setUp() {
        box = new Box(10, 10);
        cell = new Cell(1);
    }

    @Test
    public void testAddCollisionForcesToCell_JustTouching() {
        cell.setPosition(1, 1);

        box.addWallCollisionForcesToCell(cell);

        assertForce(0, 0, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_LowXYCollision() {
        cell.setPosition(0.5, 0.5);

        box.addWallCollisionForcesToCell(cell);

        assertForce(0.5, 0.5, cell);
    }

    @Test
    public void testAddCollisionForcesToCell_HighXYCollision() {
        cell.setPosition(9.5, 9.5);

        box.addWallCollisionForcesToCell(cell);

        assertForce(-0.5, -0.5, cell);
    }
}
