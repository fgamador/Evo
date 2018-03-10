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
        NewtonianBody body = new NewtonianBodyWithEnvironment ();
        body.setCenterPosition(5, -5);
        Puller puller = new Puller(body, body.getEnvironment());
        puller.setPosition(5, -5);

        puller.addForce();

        assertNetForce(0, 0, body.getEnvironment());
    }

    @Test
    public void pullWithOffsetAddsForce() {
        NewtonianBody body = new NewtonianBodyWithEnvironment();
        body.setCenterPosition(5, -5);
        Puller puller = new Puller(body, body.getEnvironment());
        puller.setPosition(6, -6);

        puller.addForce();

        assertNetForce(1, -1, body.getEnvironment());
    }

    @Test
    public void pullerForceUsesForceFactor() {
        NewtonianBody body = new NewtonianBodyWithEnvironment();
        body.setCenterPosition(5, -5);
        Puller puller = new Puller(body, body.getEnvironment());
        puller.setPosition(6, -6);
        Puller.forceFactor.setValue(2);

        puller.addForce();

        assertNetForce(2, -2, body.getEnvironment());
    }
}
