package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public class NewtonianBodyTest extends EvoTest {
    @Test
    public void stationaryBodyWithNoForcesDoesNotMove() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);

        body.subtick_outer(1);

        assertVelocity(0, 0, body);
        assertPosition(0, 0, body);
    }

    @Test
    public void movingBodyWithNoForcesCoasts() {
        NewtonianBody body = new NewtonianBody(0, 0, 0.5, -1);
        body.setMass(1);

        body.subtick_outer(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void stationaryBodyWithForceAccelerates() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.getForces().addForce(0.5, (double) -1);

        body.subtick_outer(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void twoForcesHaveAdditiveAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.getForces().addForce(0.5, (double) -1);
        body.getForces().addForce(1.5, (double) 2);

        body.subtick_outer(1);

        assertVelocity(2, 1, body);
        assertPosition(2, 1, body);
    }

    @Test
    public void doubleMassHasHalvedAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(2);
        body.getForces().addForce((double) 1, (double) -2);

        body.subtick_outer(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void speedLimitCapsVelocity() {
        NewtonianBody.speedLimit.setValue(4);
        NewtonianBody body = new NewtonianBody(0, 0, 8 / SQRT_2, -8 / SQRT_2);
        body.setMass(1);

        body.subtick_outer(1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, body);
    }

    @Test
    public void doubleResolutionSubticksMoveAtHalfVelocity() {
        NewtonianBody body = new NewtonianBody(0, 0, 1, 1);
        body.setMass(1);

        body.subtick_outer(2);

        assertVelocity(1, 1, body);
        assertPosition(0.5, 0.5, body);
    }

    @Test
    public void doubleResolutionSubticksAccelerateAtHalfForce() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.getForces().addForce((double) 1, (double) 1);

        body.subtick_outer(2);

        assertVelocity(0.5, 0.5, body);
        assertPosition(0.25, 0.25, body);
    }
}
