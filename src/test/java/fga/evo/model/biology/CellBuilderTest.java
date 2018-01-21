package fga.evo.model.biology;

import fga.evo.model.biology.Cell;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellBuilderTest {
    @Test(expected = IllegalStateException.class)
    public void testBuild_SetAreaTwice() {
        new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .setFloatRingArea(Math.PI);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild_SetOuterRadiusThenArea() {
        new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setFloatRingArea(Math.PI);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild_SetOuterRadiusTwice() {
        new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setFloatRingOuterRadius(1);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild_SetAreaThenOuterRadius() {
        new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .setFloatRingOuterRadius(1);
    }

    @Test
    public void testBuild_FloatRingOuterRadius() {
        Cell cell = new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .build();
        assertEquals(1, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(1, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(0, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingOuterRadii() {
        Cell cell = new Cell.Builder()
                .setFloatRingOuterRadius(1)
                .setPhotoRingOuterRadius(2)
                .build();
        assertEquals(2, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(2, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(4 * Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuild_FloatRingArea() {
        Cell cell = new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .build();
        assertEquals(1, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(1, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(0, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingAreas() {
        Cell cell = new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .setPhotoRingArea(3 * Math.PI)
                .build();
        assertEquals(2, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(2, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(4 * Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingAreas_ReverseOrder() {
        Cell cell = new Cell.Builder()
                .setPhotoRingArea(3 * Math.PI)
                .setFloatRingArea(Math.PI)
                .build();
        assertEquals(2, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(2, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(4 * Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0.001);
    }

    @Test
    public void testBuild_FloatRingAreaAndPhotoRingOuterRadius() {
        Cell cell = new Cell.Builder()
                .setFloatRingArea(Math.PI)
                .setPhotoRingOuterRadius(2)
                .build();
        assertEquals(2, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(2, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(4 * Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0.001);
    }
}