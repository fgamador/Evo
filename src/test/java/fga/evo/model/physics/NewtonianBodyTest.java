package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public class NewtonianBodyTest extends EvoTest {
    @Test
    public void subtickWithNoForcesDoesNotMoveStationaryBody() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(0, 0, body);
        assertPosition(0, 0, body);
    }

    @Test
    public void subtickWithNoForcesDoesNotAccelerateMovingBody() {
        NewtonianBody body = new NewtonianBody(0, 0, 0.5, -1);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void subtickWithOneForceAcceleratesAndMovesStationaryBody() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void subtickWithTwoForcesHasAdditiveAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);
        body.addForce(1.5, 2);

        body.subtick(1);

        assertVelocity(2, 1, body);
        assertPosition(2, 1, body);
    }

    @Test
    public void doubleMassHasHalfAcceleration() {
        NewtonianBody heavy = new NewtonianBody(0, 0, 0, 0);
        heavy.setMass(2);
        heavy.addForce(1, -2);

        heavy.subtick(1);

        assertVelocity(0.5, -1, heavy);
        assertPosition(0.5, -1, heavy);
    }

    @Test
    public void speedLimitCapsVelocity() {
        NewtonianBody.speedLimit.setValue(4);
        NewtonianBody body = new NewtonianBody(0, 0, 8 / SQRT_2, -8 / SQRT_2);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, body);
    }
}
