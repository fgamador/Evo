package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;

public class PullerTest extends EvoTest {
    @Before
    public void setUp() {
        Puller.forceFactor.setValue(1);
    }

    @Test
    public void subjectWithNoOffsetAddsNoForce() {
        NewtonianBody body = new NewtonianBodyWithEnvironment();
        body.setCenterPosition(5, -5);
        Puller subject = new Puller(body);
        subject.setPosition(5, -5);

        subject.addForce();

        assertNetForce(0, 0, body);
    }

    @Test
    public void pullWithOffsetAddsForce() {
        NewtonianBody body = new NewtonianBodyWithEnvironment();
        body.setCenterPosition(5, -5);
        Puller subject = new Puller(body);
        subject.setPosition(6, -6);

        subject.addForce();

        assertNetForce(1, -1, body);
    }

    @Test
    public void subjectForceUsesForceFactor() {
        NewtonianBody body = new NewtonianBodyWithEnvironment();
        body.setCenterPosition(5, -5);
        Puller subject = new Puller(body);
        subject.setPosition(6, -6);
        Puller.forceFactor.setValue(2);

        subject.addForce();

        assertNetForce(2, -2, body);
    }
}
