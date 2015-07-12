package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WorldTest {
    private World world;
    private Cell cell1, cell2;

    @Before
    public void setUp() {
        world = new World(10, 10);
        cell1 = new Cell(1, 1);
        cell2 = new Cell(1, 1);
        world.addCell(cell1);
        world.addCell(cell2);
    }

    @Test
    public void testTick_NoCollisions() {
        cell1.setPosition(3, 3);
        cell2.setPosition(7, 7);

        world.tick();

        assertPosition(3, 3, cell1);
        assertVelocity(0, 0, cell1);
        assertPosition(7, 7, cell2);
        assertVelocity(0, 0, cell2);
    }

    @Test
    public void testTick_BoxCollisions() {
        cell1.setPosition(0.5, 0.5);
        cell2.setPosition(9.5, 9.5);

        world.tick();

        assertPosition(0.75, 0.75, cell1);
        assertVelocity(0.5, 0.5, cell1);
        assertPosition(9.25, 9.25, cell2);
        assertVelocity(-0.5, -0.5, cell2);
    }

    @Test
    public void testTick_CellCollision() {
        cell1.setPosition(5, 5);
        cell2.setPosition(6, 5);

        world.tick();

        assertPosition(4.5, 5, cell1);
        assertVelocity(-1, 0, cell1);
        assertPosition(6.5, 5, cell2);
        assertVelocity(1, 0, cell2);
    }

    @Test
    public void testTick_Drag() {
        cell1.setPosition(5, 5);
        world.startDrag(cell1);

        world.setDragPoint(4, 4);
        world.tick();

        assertPosition(4.5, 4.5, cell1);
        assertVelocity(-1, -1, cell1);
        assertTrue(world.isDragging());

        world.setDragPoint(5, 5);
        world.tick();

        assertPosition(3.75, 3.75, cell1);
        assertVelocity(-0.5, -0.5, cell1);

        world.endDrag();
        world.tick();

        assertPosition(3.25, 3.25, cell1);
        assertVelocity(-0.5, -0.5, cell1);
        assertFalse(world.isDragging());
    }
}
