package fga.evo.model.environment;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IlluminationTest extends EvoTest {
    @Test
    public void transmissionAtTheSurfaceIsTheMaximum() {
        Illumination subject = new Illumination(100);
        assertEquals(1, subject.calcTransmissionFactor(0), 0);
    }

    @Test
    public void transmissionAboveTheSurfaceIsTheMaximum() {
        Illumination subject = new Illumination(100);
        assertEquals(1, subject.calcTransmissionFactor(50), 0);
    }

    @Test
    public void transmissionDecreasesWithDepth() {
        Illumination subject = new Illumination(100);
        assertEquals(0.5, subject.calcTransmissionFactor(-50), 0);
    }

    @Test
    public void transmissionAtDepthLimitIsZero() {
        Illumination subject = new Illumination(100);
        assertEquals(0, subject.calcTransmissionFactor(-100), 0);
    }

    @Test
    public void transmissionBeyondDepthLimitIsZero() {
        Illumination subject = new Illumination(100);
        assertEquals(0, subject.calcTransmissionFactor(-110), 0);
    }

    @Test
    public void intensityScalesWithTransmission() {
        Illumination.maxIntensity.setValue(2);
        Illumination subject = new Illumination(100);
        assertEquals(1, subject.calcLightIntensity(-50), 0);
    }

    @Test
    public void illuminationUpdatesCellEnvironment() {
        Illumination.maxIntensity.setValue(2);
        Illumination subject = new Illumination(100);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0, -50);

        subject.updateEnvironment(cell);

        assertEquals(1, cell.getEnvironment().getLightIntensity(), 0);
    }
}
