package fga.evo.model.environment;

import fga.evo.model.Cell;
import fga.evo.model.EvoTest;
import fga.evo.model.environment.Illumination;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static org.junit.Assert.assertEquals;

public class IlluminationTest extends EvoTest {
    private Illumination lighting;

    @Before
    public void setUp() {
        Illumination.maxIntensity.setValue(2);

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
        Illumination.maxIntensity.setValue(4);
        assertEquals(4, lighting.calcLightIntensity(0), 0);
        assertEquals(2, lighting.calcLightIntensity(-50), 0);
        assertEquals(0, lighting.calcLightIntensity(-100), 0);
    }

    @Test
    public void testIlluminateCell() {
        Cell cell = new Cell(1);
        cell.setCenterPosition(50, -50);

        lighting.addEnergyToCell(cell);

        assertEnergy(0.5, cell);
    }
}
