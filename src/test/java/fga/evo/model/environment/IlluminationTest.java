package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertEnergy;
import static org.junit.Assert.assertEquals;

public class IlluminationTest extends EvoTest {
    @Test
    public void transmissionAtTheSurfaceIsTheMaximum() {
        Illumination lighting = new Illumination(100);
        assertEquals(1, lighting.calcTransmissionFactor(0), 0);
    }

    @Test
    public void transmissionAboveTheSurfaceIsTheMaximum() {
        Illumination lighting = new Illumination(100);
        assertEquals(1, lighting.calcTransmissionFactor(50), 0);
    }

    @Test
    public void transmissionDecreasesWithDepth() {
        Illumination lighting = new Illumination(100);
        assertEquals(0.5, lighting.calcTransmissionFactor(-50), 0);
    }

    @Test
    public void transmissionAtDepthLimitIsZero() {
        Illumination lighting = new Illumination(100);
        assertEquals(0, lighting.calcTransmissionFactor(-100), 0);
    }

    @Test
    public void transmissionBeyondDepthLimitIsZero() {
        Illumination lighting = new Illumination(100);
        assertEquals(0, lighting.calcTransmissionFactor(-110), 0);
    }

    @Test
    public void intensityScalesWithTransmission() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        assertEquals(1, lighting.calcLightIntensity(-50), 0);
    }

    @Test
    public void illuminationUpdatesCellEnvironment() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0, -50);
        CellEnvironment environment = new CellEnvironment();

        lighting.updateEnvironment(environment, cell);

        assertEquals(1, environment.getLightIntensity(), 0);
    }

    // TODO almost obsolete
    @Test
    public void illuminationAddsEnergyToCell() {
        Illumination.maxIntensity.setValue(2);
        Illumination lighting = new Illumination(100);

        Cell cell = new Cell(1);
        cell.setCenterPosition(50, -50);

        lighting.addEnergyToCell(cell);

        assertEnergy(0.5, cell);
    }
}
