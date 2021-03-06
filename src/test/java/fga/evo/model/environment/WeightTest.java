package fga.evo.model.environment;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.Cell;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class WeightTest extends EvoTest {
    @Before
    public void setUp() {
        Weight.fluidDensity.setValue(0.01);
    }

    @Test
    public void neutralBuoyancy() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        setCellDensity(cell, Weight.fluidDensity.getValue());

        new Weight().addForce(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void maxBuoyancy() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        setCellDensity(cell, 0);

        new Weight().addForce(cell);

        assertNetForce(0, Weight.gravity.getValue() * Math.PI, cell);
    }

    @Test
    public void sinking() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        setCellDensity(cell, 2 * Weight.fluidDensity.getValue());

        new Weight().addForce(cell);

        double forceY = -Weight.gravity.getValue() * Math.PI;
        assertNetForce(0, forceY, cell);
    }

    @Test
    public void fullBuoyancyWhenBarelySubmerged() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -cell.getRadius());
        setCellDensity(cell, Weight.fluidDensity.getValue());

        new Weight().addForce(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void halfBuoyancyWhenHalfSubmerged() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, 0);
        setCellDensity(cell, Weight.fluidDensity.getValue());

        new Weight().addForce(cell);

        double forceY = -cell.getMass() * Weight.gravity.getValue() / 2;
        assertNetForce(0, forceY, cell);
    }

    @Test
    public void noBuoyancyWhenNotSubmerged() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, cell.getRadius());
        setCellDensity(cell, Weight.fluidDensity.getValue());

        new Weight().addForce(cell);

        double forceY = -cell.getMass() * Weight.gravity.getValue();
        assertNetForce(0, forceY, cell);
    }

    private static void setCellDensity(Cell cell, double density) {
        cell.setMass(density * cell.getArea());
    }
}
