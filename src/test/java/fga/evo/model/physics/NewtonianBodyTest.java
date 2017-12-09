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

    @Test
    public void subtickWithNoForcesDoesNotAccelerateMovingBody() {
        NewtonianBody body = new NewtonianBody(0, 0);
        body.setMass(1);
        body.setVelocity(0.5, -1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void subtickWithOneForceAcceleratesAndMovesStationaryBody() {
        NewtonianBody body = new NewtonianBody(0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void subtickWithTwoForcesHasAdditiveAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);
        body.addForce(1.5, 2);

        body.subtick(1);

        assertVelocity(2, 1, body);
        assertPosition(2, 1, body);
    }
}
