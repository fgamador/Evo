package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static fga.evo.model.Assert.assertExactEquals;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertApproxEquals(Math.PI, testSubject.getArea());
    }

    @Test
    public void settingAreaAlsoSetsMass() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertApproxEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass());
    }

    @Test
    public void canAddToArea() {
        Ring testSubject = new TestRing(1);
        testSubject.addToArea(2);
        assertApproxEquals(3, testSubject.getArea());
    }

    @Test
    public void canMultiplyAreaByFactor() {
        Ring testSubject = new TestRing(2);
        testSubject.multiplyAreaBy(0.5);
        assertApproxEquals(1, testSubject.getArea());
    }

    @Test
    public void canSetOuterRadiusBasedOnArea() {
        Ring testSubject = new TestRing(Math.PI);
        testSubject.setRadiiBasedOnArea(0);
        assertApproxEquals(1, testSubject.getOuterRadius());
    }

    @Test
    public void canSetOuterRadiusBasedOnAreaAndInnerRadius() {
        Ring testSubject = new TestRing(3 * Math.PI);
        testSubject.setRadiiBasedOnArea(1);
        assertApproxEquals(2, testSubject.getOuterRadius());
    }

    @Test
    public void whenAreaIsZeroOuterRadiusGetsSetEqualToInnerRadius() {
        Ring testSubject = new TestRing(0);
        testSubject.setRadiiBasedOnArea(1);
        assertExactEquals(testSubject.getInnerRadius(), testSubject.getOuterRadius());
    }
}
