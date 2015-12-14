package fga.evo.model;

import javafx.scene.effect.Lighting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static org.junit.Assert.assertEquals;

public class IlluminationTest {
    private Illumination lighting;
    private double defaultMaxIntensity;

    @Before
    public void setUp() {
        defaultMaxIntensity = Illumination.getMaxIntensity();
        lighting = new Illumination(100);
    }

    @After
    public void tearDown() {
        Illumination.setMaxIntensity(defaultMaxIntensity);
    }

    @Test
    public void testCalcLightIntensity() {
        Illumination.setMaxIntensity(2);
        assertEquals(2, lighting.calcLightIntensity(50), 0);
        assertEquals(2, lighting.calcLightIntensity(0), 0);
        assertEquals(1, lighting.calcLightIntensity(-50), 0);
        assertEquals(0, lighting.calcLightIntensity(-100), 0);
    }

    @Test
    public void testCalcLightIntensity_MaxIntensity() {
        Illumination.setMaxIntensity(4);
        assertEquals(4, lighting.calcLightIntensity(0), 0);
        assertEquals(2, lighting.calcLightIntensity(-50), 0);
        assertEquals(0, lighting.calcLightIntensity(-100), 0);
    }

    @Test
    public void testIlluminateCell() {
        Illumination.setMaxIntensity(2);
        Cell cell = new Cell(1);
        cell.setCenterPosition(50, -50);

        lighting.addEnergyToCell(cell);

        assertEnergy(0.5, cell);
    }
}
