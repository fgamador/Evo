package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;
import static org.junit.Assert.*;

public class DraggerTest {
    private Cell cell;
    private Dragger dragger;

    @Before
    public void setUp() {
        cell = new Cell(1, 1);
        dragger = new Dragger(cell);
    }

    @Test
    public void testAddForceToCell_NoDrag() {
        cell.setPosition(5, 5);
        dragger.setPosition(5, 5);

        dragger.addForceToCell();

        assertForce(0, 0, cell);
    }

    @Test
    public void testAddForceToCell_Drag() {
        cell.setPosition(5, 5);
        dragger.setPosition(6, 6);

        dragger.addForceToCell();

        assertForce(1, 1, cell);
    }

    @Test
    public void testAddForceToCell_DragForceFactor() {
        cell.setPosition(5, 5);
        dragger.setPosition(6, 6);

        double defaultForceFactor = Dragger.getDragForceFactor();
        try {
            Dragger.setDragForceFactor(2);
            dragger.addForceToCell();
            assertForce(2, 2, cell);
        } finally {
            Dragger.setDragForceFactor(defaultForceFactor);
        }
    }
}
