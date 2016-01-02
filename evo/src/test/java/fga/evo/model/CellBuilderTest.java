package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellBuilderTest {
    @Test
    public void testBuild_PhotoRingOuterRadius() {
        Cell cell = new Cell.Builder()
                .setPhotoRingOuterRadius(1)
                .build();
        assertEquals(1, cell.getRadius(), 0);
        assertEquals(Math.PI, cell.getArea(), 0.001);
        assertEquals(1, cell.getPhotoRingOuterRadius(), 0);
        assertEquals(Math.PI, cell.getPhotoArea(), 0.001);
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
}
