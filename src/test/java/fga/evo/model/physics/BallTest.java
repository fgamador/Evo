package fga.evo.model.physics;

import fga.evo.model.Assert;
import fga.evo.model.EvoTest;
import fga.evo.model.environment.SurroundingWalls;
import org.junit.Before;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.*;

public class BallTest extends EvoTest {
    @Before
    public void setUp() {
        NewtonianBody.speedLimit.setValue(100);
    }

    @Test
    public void areaCorrespondsToRadius() {
        Ball ball = new BallWithEnvironment();
        ball.setRadius(3);
        assertEquals(9 * Math.PI, ball.getArea(), Assert.DEFAULT_DELTA);
    }

    @Test
    public void ballCalculatesOverlapForceFromOverlapAndForceFactor() {
        Ball.overlapForceFactor.setValue(2);
        assertEquals(6, Ball.calcOverlapForce(3), 0);
    }

    @Test
    public void unbondedBallsAreNotBonded() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void bondingBallsBondsThem() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();

        ball1.addBond(ball2);

        assertTrue(ball1.isBondedTo(ball2));
        assertTrue(ball2.isBondedTo(ball1));
    }

    @Test
    public void bondingBallsReturnsBond() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();

        PairBond bond = ball1.addBond(ball2);

        assertTrue(bond.bondsTo(ball1));
        assertTrue(bond.bondsTo(ball2));
    }

    @Test
    public void unbondingBallsUnbondsThem() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        ball1.addBond(ball2);

        ball1.removeBond(ball2);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void unbondingBallsReturnsBond() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        ball1.addBond(ball2);

        PairBond bond = ball1.removeBond(ball2);

        assertTrue(bond.bondsTo(ball1));
        assertTrue(bond.bondsTo(ball2));
    }

    @Test
    public void unbondingBallsFromOtherEndUnbondsThem() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        ball1.addBond(ball2);

        ball2.removeBond(ball1);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
    }

    @Test
    public void unbondingRemovesOnlySpecifiedBond() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        Ball ball3 = new BallWithEnvironment();
        ball1.addBond(ball2);
        ball1.addBond(ball3);

        ball1.removeBond(ball2);

        assertFalse(ball1.isBondedTo(ball2));
        assertFalse(ball2.isBondedTo(ball1));
        assertTrue(ball1.isBondedTo(ball3));
        assertTrue(ball3.isBondedTo(ball1));
    }

    @Test(expected = IllegalStateException.class)
    public void cannotBondSameBallTwice() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        ball1.addBond(ball2);
        ball1.addBond(ball2);
    }

    @Test(expected = IllegalStateException.class)
    public void cannotBondSameBallTwiceEvenFromOtherEnd() {
        Ball ball1 = new BallWithEnvironment();
        Ball ball2 = new BallWithEnvironment();
        ball1.addBond(ball2);
        ball2.addBond(ball1);
    }

    @Test
    public void ballsRecordOverlap() {
        Ball ball1 = createBall(1, 0, 0);
        Ball ball2 = createBall(1, 1.5, 0);

        ball1.onOverlap(ball2, 0.5);

        assertEquals(0.5, ball1.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void ballsRecordOverlapEvenWhenBonded() {
        Ball ball1 = createBall(1, 0, 0);
        Ball ball2 = createBall(1, 1.5, 0);
        ball1.addBond(ball2);

        ball1.onOverlap(ball2, 0.5);

        assertEquals(0.5, ball1.getRecentTotalOverlap(), 0);
        assertEquals(0.5, ball2.getRecentTotalOverlap(), 0);
    }

    @Test
    public void subtickDecaysRecordedOverlap() {
        Ball ball1 = createBall(1, 0, 0);
        ball1.setMass(1);
        Ball ball2 = createBall(1, 1.5, 0);
        ball1.onOverlap(ball2, 0.5);

        ball1.subtickPhysics(2);

        assertTrue(ball1.getRecentTotalOverlap() < 0.5);
    }

    private Ball createBall(double radius, double centerX, double centerY) {
        Ball ball = new BallWithEnvironment();
        ball.setRadius(radius);
        ball.setCenterPosition(centerX, centerY);
        return ball;
    }
}
