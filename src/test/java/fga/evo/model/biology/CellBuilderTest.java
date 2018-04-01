package fga.evo.model.biology;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellBuilderTest {
    @Test(expected = IllegalStateException.class)
    public void testBuild_SetAreaTwice() {
        new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .withFloatRingArea(Math.PI);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild_SetOuterRadiusThenArea() {
        new Cell.Builder()
                .withFloatRingOuterRadius(1)
                .withFloatRingArea(Math.PI);
    }

    @Test
    public void testBuild_FloatRingOuterRadius() {
        Cell cell = new Cell.Builder()
                .withFloatRingOuterRadius(1)
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
                .withFloatRingOuterRadius(1)
                .withPhotoRingOuterRadius(2)
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
                .withFloatRingArea(Math.PI)
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
                .withFloatRingArea(Math.PI)
                .withPhotoRingArea(3 * Math.PI)
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
                .withPhotoRingArea(3 * Math.PI)
                .withFloatRingArea(Math.PI)
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
                .withFloatRingArea(Math.PI)
                .withPhotoRingOuterRadius(2)
                .build();
        assertEquals(2, cell.getRadius(), 0);
        assertEquals(1, cell.getFloatRingOuterRadius(), 0);
        assertEquals(2, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(4 * Math.PI, cell.getArea(), 0.001);
        assertEquals(Math.PI, cell.getFloatArea(), 0.001);
        assertEquals(3 * Math.PI, cell.getPhotoArea(), 0.001);
    }
}
