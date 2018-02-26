package fga.evo.model.environment;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.Cell;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class WeightTest extends EvoTest {
    private Cell cell;
    private double maxDisplacement;

    @Before
    public void setUp() {
        Weight.fluidDensity.setValue(0.01);
        cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        maxDisplacement = cell.getArea();
    }

    @Test
    public void neutralBuoyancy() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        cell.setMass(Weight.fluidDensity.getValue() * cell.getArea());
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void maxBuoyancy() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        cell.setMass(0);
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        assertNetForce(0, Weight.gravity.getValue() * Math.PI, environment);
    }

    @Test
    public void sinking() {
        Cell cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        cell.setMass(2 * Weight.fluidDensity.getValue() * cell.getArea());
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        double forceY = -Weight.gravity.getValue() * Math.PI;
        assertNetForce(0, forceY, environment);
    }

    @Test
    public void testBarelySubmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);
        cell.setCenterPosition(0, -cell.getRadius());
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        assertNetForce(0, 0, environment);
    }

    @Test
    public void testFullyEmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);
        cell.setCenterPosition(0, cell.getRadius());
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        double forceY = -cell.getMass() * Weight.gravity.getValue();
        assertNetForce(0, forceY, environment);
    }

    @Test
    public void testHalfEmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);
        cell.setCenterPosition(0, 0);
        CellEnvironment environment = new CellEnvironment();

        new Weight().updateEnvironment(environment, cell);

        double forceY = -cell.getMass() * Weight.gravity.getValue() / 2;
        assertNetForce(0, forceY, environment);
    }
}
