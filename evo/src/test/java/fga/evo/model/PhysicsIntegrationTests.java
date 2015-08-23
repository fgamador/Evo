package fga.evo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhysicsIntegrationTests extends WorldIntegrationTests {
    private double defaultTissueDensity;

    @Before
    public void setUp() {
        defaultTissueDensity = PhotoRing.parameters.getTissueDensity();
        // ensure that a cell with radius 1 has mass 1
        // TODO mock the mass
        PhotoRing.parameters.setTissueDensity(1 / Math.PI);
    }

    @After
    public void tearDown() {
        PhotoRing.parameters.setTissueDensity(defaultTissueDensity);
    }

    @Test
    public void testNoCollision() {
        world.addEnvironmentalInfluence(new Walls(10, 10));
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // no cell or wall overlap
        cell1.setPosition(3, -3);
        cell2.setPosition(7, -7);

        world.tick();

        assertVelocity(0, 0, cell1);
        assertPosition(3, -3, cell1);
        assertVelocity(0, 0, cell2);
        assertPosition(7, -7, cell2);
    }

    @Test
    public void testBoxCornerCollisions() {
        world.addEnvironmentalInfluence(new Walls(500, 500));
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // overlap walls by 0.5
        cell1.setPosition(0.5, -0.5);
        cell2.setPosition(499.5, -499.5);

        world.tick();

        assertVelocity(0.5, -0.5, cell1);
        assertPosition(1, -1, cell1);
        assertVelocity(-0.5, 0.5, cell2);
        assertPosition(499, -499, cell2);
    }

    @Test
    public void testFullWallCollision() {
        world.addEnvironmentalInfluence(new Walls(500, 500));
        Cell cell = addCell(1);
        cell.setVelocity(-1, 0);
        cell.setPosition(1, -250); // overlap 0.0, accel 0.0

        world.tick();

        assertVelocity(-1, 0, cell);
        assertPosition(0, -250, cell); // overlap 1, accel 1

        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(0, -250, cell); // overlap 1, accel 1

        world.tick();

        assertVelocity(1, 0, cell);
        assertPosition(1, -250, cell); // overlap 0, accel 0

        world.tick();

        assertVelocity(1, 0, cell);
        assertPosition(2, -250, cell); // overlap 0, accel 0
    }

    @Test
    public void testCellCollision() {
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // overlap by 1
        cell1.setPosition(5, -5);
        cell2.setPosition(6, -5);

        world.tick();

        assertVelocity(-1, 0, cell1);
        assertPosition(4, -5, cell1);
        assertVelocity(1, 0, cell2);
        assertPosition(7, -5, cell2);
    }

    @Test
    public void testFluidDrag() {
        world.addEnvironmentalInfluence(new Drag());
        Cell cell = addCell(1);
        cell.setVelocity(1, 0);

        world.tick();

        assertVelocity(1 - Drag.getDragFactor(), 0, cell);
    }

    @Test
    public void testBuoyancy_Sinking() {
        PhotoRing.parameters.setTissueDensity(defaultTissueDensity);
        world.addEnvironmentalInfluence(new Buoyancy());
        assertTrue(PhotoRing.parameters.getTissueDensity() > Buoyancy.getFluidDensity());
        Cell cell = addCell(1);

        world.tick();

        assertTrue(cell.getVelocityY() < 0);
    }

    @Test
    public void testTick_Pull() {
        Cell cell = addCell(1);
        cell.setPosition(5, -5);
        world.startPull(cell);

        world.setPullPoint(4, -4);
        world.tick();

        assertVelocity(-1, 1, cell);
        assertPosition(4, -4, cell);
        assertTrue(world.isPulling());

        world.setPullPoint(5, -5);
        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(4, -4, cell);

        world.endPull();
        world.tick();

        assertVelocity(0, 0, cell);
        assertPosition(4, -4, cell);
        assertFalse(world.isPulling());
    }

//    @Test
//    public void testThreeCellChain_PullMiddle() {
//        Cell cell1 = addCell(1, 10);
//        cell1.setPhysics(2);
//        Cell cell2 = addCell(1, 10);
//        cell2.setPhysics(2);
//        Cell cell3 = addCell(1, 10);
//        cell3.setPhysics(2);
//        cell1.addBond(cell2);
//        cell2.addBond(cell3);
//        // just touching: no bond forces
//        cell1.setPosition(180, -200);
//        cell2.setPosition(200, -200);
//        cell3.setPosition(220, -200);
//
//        world.startPull(cell2);
//        world.setPullPoint(200, -205);
//        world.tick();
//
//        assertVelocity(0, 0, cell1);
//        assertPosition(180, -200, cell1);
//        assertVelocity(0, -5, cell2);
//        assertPosition(200, -205, cell2);
//        assertVelocity(0, 0, cell3);
//        assertPosition(220, -200, cell3);
//
//        // TODO to be continued...
//    }
}
