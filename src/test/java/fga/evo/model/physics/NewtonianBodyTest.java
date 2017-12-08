package fga.evo.model.physics;

import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public class NewtonianBodyTest {
    @Test
    public void subtickWithNoForcesDoesNotMoveStationaryBody() {
        NewtonianBody body = new NewtonianBody(0, 0);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(0, 0, body);
        assertPosition(0, 0, body);
    }
}
