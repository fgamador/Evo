package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

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
        assertExactEquals(1, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(1, cell.getPhotoRingOuterRadius());
        assertApproxEquals(Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(0, cell.getPhotoArea());
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingOuterRadii() {
        Cell cell = new Cell.Builder()
                .withFloatRingOuterRadius(1)
                .withPhotoRingOuterRadius(2)
                .build();
        assertExactEquals(2, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(2, cell.getPhotoRingOuterRadius());
        assertApproxEquals(4 * Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(3 * Math.PI, cell.getPhotoArea());
    }

    @Test
    public void testBuild_FloatRingArea() {
        Cell cell = new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .build();
        assertExactEquals(1, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(1, cell.getPhotoRingOuterRadius());
        assertApproxEquals(Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(0, cell.getPhotoArea());
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingAreas() {
        Cell cell = new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .withPhotoRingArea(3 * Math.PI)
                .build();
        assertExactEquals(2, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(2, cell.getPhotoRingOuterRadius());
        assertApproxEquals(4 * Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(3 * Math.PI, cell.getPhotoArea());
    }

    @Test
    public void testBuild_FloatRingAndPhotoRingAreas_ReverseOrder() {
        Cell cell = new Cell.Builder()
                .withPhotoRingArea(3 * Math.PI)
                .withFloatRingArea(Math.PI)
                .build();
        assertExactEquals(2, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(2, cell.getPhotoRingOuterRadius());
        assertApproxEquals(4 * Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(3 * Math.PI, cell.getPhotoArea());
    }

    @Test
    public void testBuild_FloatRingAreaAndPhotoRingOuterRadius() {
        Cell cell = new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .withPhotoRingOuterRadius(2)
                .build();
        assertExactEquals(2, cell.getRadius());
        assertExactEquals(1, cell.getFloatRingOuterRadius());
        assertExactEquals(2, cell.getPhotoRingOuterRadius());
        assertApproxEquals(4 * Math.PI, cell.getArea());
        assertApproxEquals(Math.PI, cell.getFloatArea());
        assertApproxEquals(3 * Math.PI, cell.getPhotoArea());
    }
}
