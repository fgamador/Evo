package fga.evo.model;

import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertForce;

public class BuoyancyTest {
    private Buoyancy buoyancy;
    private Cell cell;
    private double displacement;

    @Before
    public void setUp() {
        buoyancy = new Buoyancy();
        cell = new Cell(10);
        displacement = cell.getArea();
    }

    @Test
    public void testNeutralBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = Buoyancy.getFluidDensity() * displacement;
        }};

        buoyancy.addForcesToCell(cell);

        assertForce(0, 0, cell);
    }

    @Test
    public void testMaxBuoyancy() {
        new Expectations(cell) {{
            cell.getMass();
            result = 0;
        }};

        buoyancy.addForcesToCell(cell);

        assertForce(0, -Buoyancy.getGravity() * Math.PI, cell);
    }

    @Test
    public void testSinking() {
        new Expectations(cell) {{
            cell.getMass();
            result = 2 * Buoyancy.getFluidDensity() * displacement;
        }};

        buoyancy.addForcesToCell(cell);

        assertForce(0, Buoyancy.getGravity() * Math.PI, cell);
    }
}
