package fga.evo.model;

import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class WeightTest {
    private Weight weight;
    private Cell cell;
    private double maxDisplacement;

    @Before
    public void setUp() {
        weight = new Weight();
        cell = new Cell(10);
        cell.setCenterPosition(0, -100);
        maxDisplacement = cell.getArea();
    }

    @Test
    public void testNeutralBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Weight.fluidDensity.getValue() * maxDisplacement;
        }};

        weight.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testMaxBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = 0;
        }};

        weight.addForcesToCell(cell);

        assertNetForce(0, Weight.gravity.getValue() * Math.PI, cell);
    }

    @Test
    public void testSinking() {
        new Expectations(cell) {{
            cell.getMass();
            result = 2 * Weight.fluidDensity.getValue() * maxDisplacement;
        }};

        weight.addForcesToCell(cell);

        assertNetForce(0, -Weight.gravity.getValue() * Math.PI, cell);
    }

    @Test
    public void testBarelySubmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Weight.fluidDensity.getValue() * maxDisplacement;
        }};

        cell.setCenterPosition(0, -cell.getRadius());
        weight.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testFullyEmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Weight.fluidDensity.getValue() * maxDisplacement;
        }};

        cell.setCenterPosition(0, cell.getRadius());
        weight.addForcesToCell(cell);

        assertNetForce(0, -cell.getMass() * Weight.gravity.getValue(), cell);
    }

    @Test
    public void testHalfEmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Weight.fluidDensity.getValue() * maxDisplacement;
        }};

        cell.setCenterPosition(0, 0);
        weight.addForcesToCell(cell);

        assertNetForce(0, -cell.getMass() * Weight.gravity.getValue() / 2, cell);
    }
}
