package fga.evo.model.environment;

import fga.evo.model.EvoTest;
import fga.evo.model.biology.Cell;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class WeightTest extends EvoTest {
    private Weight weight;
    private Cell cell;
    private double maxDisplacement;

    @Before
    public void setUp() {
        Weight.fluidDensity.setValue(0.01);
        weight = new Weight();
        cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        maxDisplacement = cell.getArea();
    }

    @Test
    public void testNeutralBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);

        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testMaxBuoyancy() {
        cell.setMass(0);

        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, Weight.gravity.getValue() * Math.PI, cell);
    }

    @Test
    public void testSinking() {
        cell.setMass(2 * Weight.fluidDensity.getValue() * maxDisplacement);

        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, -Weight.gravity.getValue() * Math.PI, cell);
    }

    @Test
    public void testBarelySubmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);

        cell.setCenterPosition(0, -cell.getRadius());
        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testFullyEmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);

        cell.setCenterPosition(0, cell.getRadius());
        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, -cell.getMass() * Weight.gravity.getValue(), cell);
    }

    @Test
    public void testHalfEmergedBuoyancy() {
        cell.setMass(Weight.fluidDensity.getValue() * maxDisplacement);

        cell.setCenterPosition(0, 0);
        weight.updateEnvironment(cell.getEnvironment(), cell);

        assertNetForce(0, -cell.getMass() * Weight.gravity.getValue() / 2, cell);
    }
}
