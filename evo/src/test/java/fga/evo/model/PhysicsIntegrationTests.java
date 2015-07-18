package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhysicsIntegrationTests {
    private World world;

    @Before
    public void setUp() {
        world = new World(500, 500);
    }

    @Test
    public void testNoCollision() {
        Cell cell1 = addCell(1, 1);
        Cell cell2 = addCell(1, 1);
        // no overlap
        cell1.setPosition(3, 3);
        cell2.setPosition(7, 7);

        world.tick();

        assertVelocity(0, 0, cell1);
        assertPosition(3, 3, cell1);
        assertVelocity(0, 0, cell2);
        assertPosition(7, 7, cell2);
    }

    @Test
    public void testBoxCornerCollisions() {
        Cell cell1 = addCell(1, 1);
        Cell cell2 = addCell(1, 1);
        // overlap walls by 0.5
        cell1.setPosition(0.5, 0.5);
        cell2.setPosition(499.5, 499.5);

        world.tick();

        assertVelocity(0.5, 0.5, cell1);
        assertPosition(1, 1, cell1);
        assertVelocity(-0.5, -0.5, cell2);
        assertPosition(499, 499, cell2);
    }

    @Test
    public void testCellCollision() {
        Cell cell1 = addCell(1, 1);
        Cell cell2 = addCell(1, 1);
        // overlap by 1
        cell1.setPosition(5, 5);
        cell2.setPosition(6, 5);

        world.tick();

        assertVelocity(-1, 0, cell1);
        assertPosition(4, 5, cell1);
        assertVelocity(1, 0, cell2);
        assertPosition(7, 5, cell2);
    }

    @Test
    public void testTick_Drag() {
        Cell cell = addCell(1, 1);
        cell.setPosition(5, 5);
        world.startDrag(cell);

        world.setDragPoint(4, 4);
        world.tick();

        assertVelocity(-1, -1, cell);
        assertPosition(4, 4, cell);
        assertTrue(world.isDragging());

        world.setDragPoint(5, 5);
        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(4, 4, cell);

        world.endDrag();
        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(4, 4, cell);
        assertFalse(world.isDragging());
    }

    @Test
    public void testFullWallCollision() {
        Cell cell = addCell(1, 10);
        cell.setVelocity(-1, 0);
        cell.setPosition(10, 250); // overlap 0.0, accel 0.0

        world.tick();

        assertVelocity(-1, 0, cell);
        assertPosition(9, 250, cell); // overlap 1, accel 1

        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(9, 250, cell); // overlap 1, accel 1

        world.tick();

        assertVelocity(1, 0, cell);
        assertPosition(10, 250, cell); // overlap 0, accel 0

        world.tick();

        assertVelocity(1, 0, cell);
        assertPosition(11, 250, cell); // overlap 0, accel 0
    }

    private Cell addCell(double mass, double radius) {
        Cell cell = new Cell(mass, radius);
        world.addCell(cell);
        return cell;
    }
}
