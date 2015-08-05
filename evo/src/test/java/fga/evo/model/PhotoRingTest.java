package fga.evo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoRingTest {
    @Test
    public void testCalcPhotoAbsorptivity() {
        assertEquals(0.5, new PhotoRing(1, 0).calcPhotoAbsorptivity(), 0);
        assertEquals(0.75, new PhotoRing(3, 0).calcPhotoAbsorptivity(), 0);
    }

    @Test
    public void testPhotosynthesize() {
        assertEquals(4.5, new PhotoRing(3, 0).photosynthesize(2), 0);
    }
}
