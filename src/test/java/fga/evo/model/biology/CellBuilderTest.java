package fga.evo.model.biology;

import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

public class CellBuilderTest {
    @Test(expected = IllegalStateException.class)
    public void cannotSetAreaTwice() {
        new Cell.Builder()
                .withFloatRingArea(Math.PI)
                .withFloatRingArea(Math.PI);
    }

    @Test(expected = IllegalStateException.class)
    public void cannotSetOuterRadiusThenArea() {
        new Cell.Builder()
                .withFloatRingOuterRadius(1)
                .withFloatRingArea(Math.PI);
    }

    @Test
    public void setsAllFromFloatRingOuterRadius() {
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
    public void setsAllFromFloatRingAndPhotoRingOuterRadii() {
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
    public void setsAllFromFloatRingArea() {
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
    public void setsAllFromFloatRingAndPhotoRingAreas() {
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
    public void setsAllFromFloatRingAndPhotoRingAreasSpecifiedInReverseOrder() {
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
    public void setsAllFromFloatRingAreaAndPhotoRingOuterRadius() {
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
