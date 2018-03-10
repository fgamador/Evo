package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public class NewtonianBodyTest extends EvoTest {
    @Test
    public void stationaryBodyWithNoForcesDoesNotMove() {
        NewtonianBody body = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        body.setMass(1);

        body.subtick(1);

        assertVelocity(0, 0, body);
        assertPosition(0, 0, body);
    }

    @Test
    public void movingBodyWithNoForcesCoasts() {
        NewtonianBody body = new NewtonianBody(0, 0, 0.5, -1);
        body.setMass(1);

        body.subtick(new NewtonianBodyEnvironment(), 1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void stationaryBodyWithForceAccelerates() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        environment.addForce(0.5, -1);

        body.subtick(environment, 1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void twoForcesHaveAdditiveAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        environment.addForce(0.5, -1);
        environment.addForce(1.5, 2);

        body.subtick(environment, 1);

        assertVelocity(2, 1, body);
        assertPosition(2, 1, body);
    }

    @Test
    public void doubleMassHasHalvedAcceleration() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(2);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        environment.addForce(1, -2);

        body.subtick(environment, 1);

        assertVelocity(0.5, -1, body);
        assertPosition(0.5, -1, body);
    }

    @Test
    public void speedLimitCapsVelocity() {
        NewtonianBody.speedLimit.setValue(4);
        NewtonianBody body = new NewtonianBody(0, 0, 8 / SQRT_2, -8 / SQRT_2);
        body.setMass(1);

        body.subtick(new NewtonianBodyEnvironment(), 1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, body);
    }

    @Test
    public void doubleResolutionSubticksMoveAtHalfVelocity() {
        NewtonianBody body = new NewtonianBody(0, 0, 1, 1);
        body.setMass(1);

        body.subtick(new NewtonianBodyEnvironment(), 2);

        assertVelocity(1, 1, body);
        assertPosition(0.5, 0.5, body);
    }

    @Test
    public void doubleResolutionSubticksAccelerateAtHalfForce() {
        NewtonianBody body = new NewtonianBody(0, 0, 0, 0);
        body.setMass(1);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        environment.addForce(1, 1);

        body.subtick(environment, 2);

        assertVelocity(0.5, 0.5, body);
        assertPosition(0.25, 0.25, body);
    }

    private static class NewtonianBodyWithEnvironment extends NewtonianBody {
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();

        public NewtonianBodyWithEnvironment(double centerX, double centerY, double velocityX, double velocityY) {
            super(centerX, centerY, velocityX, velocityY);
        }

        @Override
        public NewtonianBodyEnvironment getEnvironment() {
            return environment;
        }
    }
}
