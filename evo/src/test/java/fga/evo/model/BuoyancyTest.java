package fga.evo.model;

import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class BuoyancyTest {
    private Buoyancy buoyancy;
    private Cell cell;
    private double maxDisplacement;

    @Before
    public void setUp() {
        buoyancy = new Buoyancy();
        cell = new Cell(10);
        cell.setPosition(0, -100);
        maxDisplacement = cell.getArea();
    }

    @Test
    public void testNeutralBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Buoyancy.getFluidDensity() * maxDisplacement;
        }};

        buoyancy.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testMaxBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = 0;
        }};

        buoyancy.addForcesToCell(cell);

        assertNetForce(0, Buoyancy.getGravity() * Math.PI, cell);
    }

    @Test
    public void testSinking() {
        new Expectations(cell) {{
            cell.getMass();
            result = 2 * Buoyancy.getFluidDensity() * maxDisplacement;
        }};

        buoyancy.addForcesToCell(cell);

        assertNetForce(0, -Buoyancy.getGravity() * Math.PI, cell);
    }

    @Test
    public void testBarelySubmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Buoyancy.getFluidDensity() * maxDisplacement;
        }};

        cell.setPosition(0, -cell.getRadius());
        buoyancy.addForcesToCell(cell);

        assertNetForce(0, 0, cell);
    }

    @Test
    public void testFullyEmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Buoyancy.getFluidDensity() * maxDisplacement;
        }};

        cell.setPosition(0, cell.getRadius());
        buoyancy.addForcesToCell(cell);

        assertNetForce(0, -cell.getMass() * Buoyancy.getGravity(), cell);
    }

    @Test
    public void testHalfEmergedBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Buoyancy.getFluidDensity() * maxDisplacement;
        }};

        cell.setPosition(0, 0);
        buoyancy.addForcesToCell(cell);

        assertNetForce(0, -cell.getMass() * Buoyancy.getGravity() / 2, cell);
    }
}
