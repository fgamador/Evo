package fga.evo.model;

import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class IlluminationTest {
    private Illumination lighting;

    @Before
    public void setUp() {
        lighting = new Illumination(100);
    }

    @Test
    public void testCalcLightIntensity() {
        assertEquals(2, lighting.calcLightIntensity(50), 0);
        assertEquals(2, lighting.calcLightIntensity(0), 0);
        assertEquals(1, lighting.calcLightIntensity(-50), 0);
        assertEquals(0, lighting.calcLightIntensity(-100), 0);
    }

    @Test
    public void testCalcLightIntensity_MaxIntensity() {
        double defaultMaxIntensity = Illumination.getMaxIntensity();
        try {
            Illumination.setMaxIntensity(4);
            assertEquals(4, lighting.calcLightIntensity(0), 0);
            assertEquals(2, lighting.calcLightIntensity(-50), 0);
            assertEquals(0, lighting.calcLightIntensity(-100), 0);
        } finally {
            Illumination.setMaxIntensity(defaultMaxIntensity);
        }
    }

    @Test
    public void testIlluminateCell() {
        Cell cell = new Cell(1);
        cell.setPosition(50, -50);

        lighting.addEnergyToCell(cell);

        assertEnergy(0.5, cell);
    }
}
