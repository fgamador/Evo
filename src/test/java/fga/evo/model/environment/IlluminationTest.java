package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static org.junit.Assert.assertEquals;

public class IlluminationTest extends EvoTest {
    @Test
    public void intensityAtTheSurfaceIsTheMaximum() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(2, lighting.calcLightIntensity(0), 0);
    }

    @Test
    public void intensityAboveTheSurfaceIsTheMaximum() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(2, lighting.calcLightIntensity(50), 0);
    }

    @Test
    public void intensityScalesWithDepth() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(1, lighting.calcLightIntensity(-50), 0);
    }

    @Test
    public void intensityAtDepthLimitIsZero() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(0, lighting.calcLightIntensity(-100), 0);
    }

    //@Test
    public void intensityBeyondDepthLimitIsZero() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(0, lighting.calcLightIntensity(-110), 0);
    }

    @Test
    public void illuminationUpdatesCellEnvironment() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        StandaloneCellEnvironment environment = new StandaloneCellEnvironment(0, -50);

        lighting.updateEnvironment(environment);

        assertEquals(1, environment.getLightIntensity(), 0);
    }

    // TODO almost obsolete
    @Test
    public void testIlluminateCell() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);

        Cell cell = new Cell(1);
        cell.setCenterPosition(50, -50);

        lighting.addEnergyToCell(cell);

        assertEnergy(0.5, cell);
    }
}
