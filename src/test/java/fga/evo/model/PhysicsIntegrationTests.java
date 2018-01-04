package fga.evo.model;

import fga.evo.model.environment.Drag;
import fga.evo.model.environment.SurroundingWalls;
import fga.evo.model.environment.Weight;
import fga.evo.model.physics.Puller;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhysicsIntegrationTests extends WorldIntegrationTests {
    private int defaultSubticksPerTick;

    @Before
    public void setUp() {
        Weight.fluidDensity.setValue(0.01);
        Puller.forceFactor.setValue(1);
        defaultSubticksPerTick = World.getSubticksPerTick();

        // ensure that a cell with radius 1 has mass 1
        // TODO mock the mass (or make these ball tests?)
        PhotoRing.parameters.density.setValue(1 / Math.PI);
    }

    @After
    public void tearDown() {
        World.setSubticksPerTick(defaultSubticksPerTick);
    }

    @Test
    public void testNoCollision() {
        world.addEnvironmentalInfluence(new SurroundingWalls(0, 10, -10, 0));
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // no cell or wall overlap
        cell1.setCenterPosition(3, -3);
        cell2.setCenterPosition(7, -7);

        world.tick();

        assertVelocity(0, 0, cell1);
        assertPosition(3, -3, cell1);
        assertVelocity(0, 0, cell2);
        assertPosition(7, -7, cell2);
    }

    @Test
    public void testBoxCornerCollisions() {
        World.setSubticksPerTick(1);
        world.addEnvironmentalInfluence(new SurroundingWalls(0, 500, -500, 0));
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // overlap walls by 0.5
        cell1.setCenterPosition(0.5, -0.5);
        cell2.setCenterPosition(499.5, -499.5);

        world.tick();

        assertVelocity(0.5, -0.5, cell1);
        assertPosition(1, -1, cell1);
        assertVelocity(-0.5, 0.5, cell2);
        assertPosition(499, -499, cell2);
    }

    @Test
    public void testFullWallCollision() {
        World.setSubticksPerTick(1);
        world.addEnvironmentalInfluence(new SurroundingWalls(0, 500, -500, 0));
        Cell cell = addCell(1);
        cell.setVelocity(-1, 0);
        cell.setCenterPosition(1, -250); // overlap 0.0, accel 0.0

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
    public void testMultipleWallCollisions_DoubleResolution() {
        World.setSubticksPerTick(2);
        world.addEnvironmentalInfluence(new SurroundingWalls(0, 3, -20, 0));
        Cell cell = addCell(1);
        cell.setVelocity(-1, 0);
        cell.setCenterPosition(1, -10);

        for (int i = 0; i < 1000; i++) {
            world.tick();
            if (1 < cell.getCenterX() && cell.getCenterX() < 2) {
                if (cell.getVelocityX() > 0) {
                    assertEquals(1, cell.getVelocityX(), 0.05);
                } else {
                    assertEquals(-1, cell.getVelocityX(), 0.05);
                }
            }
        }
    }

    @Test
    public void testCellCollision() {
        World.setSubticksPerTick(1);
        Cell cell1 = addCell(1);
        Cell cell2 = addCell(1);
        // overlap by 1
        cell1.setCenterPosition(5, -5);
        cell2.setCenterPosition(6, -5);

        world.tick();

        assertVelocity(-1, 0, cell1);
        assertPosition(4, -5, cell1);
        assertVelocity(1, 0, cell2);
        assertPosition(7, -5, cell2);
    }

//    @Test
//    public void testCellBond() {
//        Cell cell1 = addCell(10);
//        Cell cell2 = addCell(10);
//        cell1.addBond(cell2);
//        // overlap by 1
//        cell1.setCenterPosition(10, 0);
//        cell2.setCenterPosition(19, 0);
//
//        world.tick();
//    }

    @Test
    public void testFluidDrag() {
        world.addEnvironmentalInfluence(new Drag());
        Cell cell = addCell(1);
        cell.setVelocity(1, 0);

        world.tick();

        assertVelocity(1 - Drag.dragFactor.getValue(), 0, cell);
    }

    @Test
    public void testBuoyancy_Sinking() {
        PhotoRing.parameters.density.revertToDefaultValue();
        world.addEnvironmentalInfluence(new Weight());
        assertTrue(PhotoRing.parameters.density.getValue() > Weight.fluidDensity.getValue());
        Cell cell = addCell(1);

        world.tick();

        assertTrue(cell.getVelocityY() < 0);
    }

    @Test
    public void testTick_Pull() {
        World.setSubticksPerTick(1);
        Cell cell = addCell(1);
        cell.setCenterPosition(5, -5);
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
}
