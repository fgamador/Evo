package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import fga.evo.model.environment.StandaloneNewtonianBodyEnvironment;
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
        StandaloneNewtonianBodyEnvironment environment = new StandaloneNewtonianBodyEnvironment(5, -5);
        Puller puller = new Puller(environment);
        puller.setPosition(5, -5);

        puller.addForce();

        assertNetForce(0, 0, environment);
    }

    @Test
    public void pullWithOffsetAddsForce() {
        StandaloneNewtonianBodyEnvironment environment = new StandaloneNewtonianBodyEnvironment(5, -5);
        Puller puller = new Puller(environment);
        puller.setPosition(6, -6);

        puller.addForce();

        assertNetForce(1, -1, environment);
    }

    @Test
    public void pullerForceUsesForceFactor() {
        StandaloneNewtonianBodyEnvironment environment = new StandaloneNewtonianBodyEnvironment(5, -5);
        Puller puller = new Puller(environment);
        puller.setPosition(6, -6);
        Puller.forceFactor.setValue(2);

        puller.addForce();

        assertNetForce(2, -2, environment);
    }

}
