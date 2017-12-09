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

        body.subtick(1);

        assertVelocity(0, 0, body);
        assertPosition(0, 0, body);
    }

    @Test
    public void movingBodyWithNoForcesCoasts() {
        NewtonianBody body = new NewtonianBody(0, 0, 0.5, -1);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void stationaryBodyWithAForceAccelerates() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void twoForcesHaveAdditiveAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.addForce(0.5, -1);
        body.addForce(1.5, 2);

        body.subtick(1);

        assertVelocity(2, 1, body);
        assertPosition(2, 1, body);
    }

    @Test
    public void doubleMassHasHalvedAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(2);
        body.addForce(1, -2);

        body.subtick(1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void speedLimitCapsVelocity() {
        NewtonianBody.speedLimit.setValue(4);
        NewtonianBody body = new NewtonianBody(0, 0, 8 / SQRT_2, -8 / SQRT_2);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, body);
    }

    @Test
    public void testSubtickPhysics_DoubleResolution_ConstantVelocity() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.setVelocity(1, 1);

        body.subtick(2);

        assertVelocity(1, 1, body);
        assertPosition(0.5, 0.5, body);

        body.subtick(2);

        assertVelocity(1, 1, body);
        assertPosition(1, 1, body);
    }

    @Test
    public void testSubtickPhysics_DoubleResolution_ConstantForce() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        body.addForce(1, 1);
        body.subtick(2);

        assertVelocity(0.5, 0.5, body);
        assertPosition(0.25, 0.25, body);

        body.addForce(1, 1);
        body.subtick(2);

        assertVelocity(1, 1, body);
        assertPosition(0.75, 0.75, body);
    }
}
