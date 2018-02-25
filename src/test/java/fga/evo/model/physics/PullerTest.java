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
    public void pullerWithNoOffsetAddsNoForce() {
        NewtonianBody body = new NewtonianBody();
        body.setCenterPosition(5, -5);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        Puller puller = new Puller(body, environment);
        puller.setPosition(5, -5);

        puller.addForce();

        assertNetForce(0, 0, environment);
    }

    @Test
    public void pullWithOffsetAddsForce() {
        NewtonianBody body = new NewtonianBody();
        body.setCenterPosition(5, -5);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        Puller puller = new Puller(body, environment);
        puller.setPosition(6, -6);

        puller.addForce();

        assertNetForce(1, -1, environment);
    }

    @Test
    public void pullerForceUsesForceFactor() {
        NewtonianBody body = new NewtonianBody();
        body.setCenterPosition(5, -5);
        NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();
        Puller puller = new Puller(body, environment);
        puller.setPosition(6, -6);
        Puller.forceFactor.setValue(2);

        puller.addForce();

        assertNetForce(2, -2, environment);
    }
}
