package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoRingTest {
    @Test
    public void testCalcPhotoAbsorptivity() {
        assertEquals(0.5, new PhotoRing(1).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new PhotoRing(3).calcPhotoAbsorptivity(), 0);
        // TODO test with non-zero inner area (will fail!)
    }

    @Test
    public void testPhotosynthesize() {
        assertEquals(4.5, new PhotoRing(3).photosynthesize(2), 0);
    }
}
