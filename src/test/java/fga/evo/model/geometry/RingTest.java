package fga.evo.model.geometry;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RingTest extends EvoTest {
    @Test
    public void canSetArea() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertEquals(Math.PI, testSubject.getArea(), 0.001);
    }

    @Test
    public void settingAreaAlsoSetsMass() {
        Ring testSubject = new TestRing(0);
        testSubject.setArea(Math.PI);
        assertEquals(Math.PI * TestRing.parameters.density.getValue(), testSubject.getMass(), 0.001);
    }

    @Test
    public void canAddToArea() {
        Ring testSubject = new TestRing(1);
        testSubject.addToArea(2);
        assertEquals(3, testSubject.getArea(), 0.001);
    }

    @Test
    public void canMultiplyAreaByFactor() {
        Ring testSubject = new TestRing(2);
        testSubject.multiplyAreaBy(0.5);
        assertEquals(1, testSubject.getArea(), 0.001);
    }

    @Test
    public void canSetOuterRadiusBasedOnArea() {
        Ring testSubject = new TestRing(Math.PI);
        testSubject.setRadiiBasedOnArea(0);
        assertEquals(1, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void canSetOuterRadiusBasedOnAreaAndInnerRadius() {
        Ring testSubject = new TestRing(3 * Math.PI);
        testSubject.setRadiiBasedOnArea(1);
        assertEquals(2, testSubject.getOuterRadius(), 0.001);
    }

    @Test
    public void whenAreaIsZeroOuterRadiusGetsSetEqualToInnerRadius() {
        Ring testSubject = new TestRing(0);
        testSubject.setRadiiBasedOnArea(1);
        assertEquals(testSubject.getInnerRadius(), testSubject.getOuterRadius(), 0);
    }
}
