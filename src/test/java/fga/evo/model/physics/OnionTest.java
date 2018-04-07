package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import fga.evo.model.geometry.TestRing;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;

public class OnionTest extends EvoTest {
    @Test
    public void syncFieldsWithOneRing() {
        TestRing ring = new TestRing(Math.PI);
        Onion<TestRing> subject = new OnionWithEnvironment();
        subject.addRing(ring);

        subject.syncFields();

        assertApproxEquals(1, ring.getOuterRadius());
        assertApproxEquals(Math.PI * TestRing.parameters.density.getValue(), ring.getMass());
        assertApproxEquals(ring.getOuterRadius(), subject.getRadius());
        assertApproxEquals(ring.getArea(), subject.getArea());
        assertApproxEquals(ring.getMass(), subject.getMass());
    }

    @Test
    public void syncFieldsWithTwoRings() {
        TestRing ring1 = new TestRing(Math.PI);
        TestRing ring2 = new TestRing(3 * Math.PI);
        Onion<TestRing> subject = new OnionWithEnvironment();
        subject.addRing(ring1);
        subject.addRing(ring2);

        subject.syncFields();

        assertApproxEquals(Math.PI, ring1.getArea());
        assertApproxEquals(Math.PI * TestRing.parameters.density.getValue(), ring1.getMass());
        assertApproxEquals(3 * Math.PI, ring2.getArea());
        assertApproxEquals(3 * Math.PI * TestRing.parameters.density.getValue(), ring2.getMass());
        assertApproxEquals(ring2.getOuterRadius(), subject.getRadius());
        assertApproxEquals(ring1.getArea() + ring2.getArea(), subject.getArea());
        assertApproxEquals(ring1.getMass() + ring2.getMass(), subject.getMass());
    }

    private static class OnionWithEnvironment extends Onion<TestRing> {
        BallEnvironment environment = new BallEnvironment();

        @Override
        public BallEnvironment getEnvironment() {
            return environment;
        }
    }
}
