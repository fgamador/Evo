package fga.evo.model.physics;

import fga.evo.model.EvoTest;
import org.junit.Test;

import static fga.evo.model.Assert.assertApproxEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BallTest extends EvoTest {
    @Test
    public void areaCorrespondsToRadius() {
        Ball ball = new BallWithEnvironment();
        ball.setRadius(3);
        assertApproxEquals(9 * Math.PI, ball.getArea());
    }

    @Test
    public void ballCalculatesOverlapForceFromOverlapAndForceFactor() {
        Ball.overlapForceFactor.setValue(2);
        Ball ball = new BallWithEnvironment();
        assertApproxEquals(6, ball.calcElasticDeformationForce(3));
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
        Ball ball1 = createBallAtX(0);
        Ball ball2 = createBallAtX(1.5);

        ball1.onOverlap(ball2, 0.5);

        assertApproxEquals(0.5, ball1.getEnvironment().getAndClearTotalOverlap());
        assertApproxEquals(0.5, ball2.getEnvironment().getAndClearTotalOverlap());
    }

    @Test
    public void ballsRecordOverlapEvenWhenBonded() {
        Ball ball1 = createBallAtX(0);
        Ball ball2 = createBallAtX(1.5);
        ball1.addBond(ball2);

        ball1.onOverlap(ball2, 0.5);

        assertApproxEquals(0.5, ball1.getEnvironment().getAndClearTotalOverlap());
        assertApproxEquals(0.5, ball2.getEnvironment().getAndClearTotalOverlap());
    }

    private Ball createBallAtX(double centerX) {
        Ball ball = new BallWithEnvironment();
        ball.setRadius(1);
        ball.setCenterPosition(centerX, 0);
        return ball;
    }
}
