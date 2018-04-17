package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertPosition;
import static fga.evo.model.Assert.assertVelocity;

public class NewtonianBodyTest extends EvoTest {
    @Test
    public void stationaryBodyWithNoForcesDoesNotMove() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        subject.setMass(1);

        subject.subtick(1);

        assertVelocity(0, 0, subject);
        assertPosition(0, 0, subject);
    }

    @Test
    public void movingBodyWithNoForcesCoasts() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0.5, -1);
        subject.setMass(1);

        subject.subtick(1);

        assertVelocity(0.5, -1, subject);
        assertPosition(0.5, -1, subject);
    }

    @Test
    public void stationaryBodyWithForceAccelerates() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        subject.setMass(1);
        subject.addForce(0.5, -1);

        subject.subtick(1);

        assertVelocity(0.5, -1, subject);
        assertPosition(0.5, -1, subject);
    }

    @Test
    public void twoForcesHaveAdditiveAcceleration() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        subject.setMass(1);
        subject.addForce(0.5, -1);
        subject.addForce(1.5, 2);

        subject.subtick(1);

        assertVelocity(2, 1, subject);
        assertPosition(2, 1, subject);
    }

    @Test
    public void doubleMassHasHalvedAcceleration() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        subject.setMass(2);
        subject.addForce(1, -2);

        subject.subtick(1);

        assertVelocity(0.5, -1, subject);
        assertPosition(0.5, -1, subject);
    }

    @Test
    public void speedLimitCapsVelocity() {
        NewtonianBody.speedLimit.setValue(4);
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 8 / SQRT_2, -8 / SQRT_2);
        subject.setMass(1);

        subject.subtick(1);

        assertVelocity(4 / SQRT_2, -4 / SQRT_2, subject);
    }

    @Test
    public void doubleResolutionSubticksMoveAtHalfVelocity() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 1, 1);
        subject.setMass(1);

        subject.subtick(2);

        assertVelocity(1, 1, subject);
        assertPosition(0.5, 0.5, subject);
    }

    @Test
    public void doubleResolutionSubticksAccelerateAtHalfForce() {
        NewtonianBody subject = new NewtonianBodyWithEnvironment(0, 0, 0, 0);
        subject.setMass(1);
        subject.addForce(1, 1);

        subject.subtick(2);

        assertVelocity(0.5, 0.5, subject);
        assertPosition(0.25, 0.25, subject);
    }
}
