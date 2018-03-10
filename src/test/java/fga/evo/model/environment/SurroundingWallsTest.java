package fga.evo.model.environment;

import fga.evo.model.biology.Cell;
import fga.evo.model.EvoTest;
import fga.evo.model.physics.Ball;
import fga.evo.model.physics.BallWithEnvironment;
import org.junit.Test;

import static fga.evo.model.Assert.assertNetForce;
import static org.junit.Assert.assertEquals;

public class SurroundingWallsTest extends EvoTest {
    @Test
    public void justTouchingAddsNoForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(1, -1);

        walls.updateEnvironment(cell);

        assertNetForce(0, 0, cell.getEnvironment());
    }

    @Test
    public void lowXYCollisionAddsExpectedForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0.5, -0.5);

        walls.updateEnvironment(cell);

        assertNetForce(0.5, -0.5, cell.getEnvironment());
    }

    @Test
    public void highXYCollisionAddsExpectedForce() {
        SurroundingWalls walls = new SurroundingWalls(0, 10, -10, 0);
        Cell cell = new Cell(1);
        cell.setCenterPosition(9.5, -9.5);

        walls.updateEnvironment(cell);

        assertNetForce(-0.5, 0.5, cell.getEnvironment());
    }

    @Test
    public void highCeilingAddsNoForce() {
        SurroundingWalls walls = new SurroundingWalls(-5, 5, -5, 5);
        Cell cell = new Cell(1);
        cell.setCenterPosition(0, 0);

        walls.updateEnvironment(cell);

        assertNetForce(0, 0, cell.getEnvironment());
    }

    @Test
    public void leftBarrierCollisionRecordsOverlap() {
        Ball ball = createBall(1, 0, 0);
        SurroundingWalls.addLeftBarrierCollisionForce(ball, -0.5);
        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void rightBarrierCollisionRecordsOverlap() {
        Ball ball = createBall(1, 0, 0);
        SurroundingWalls.addRightBarrierCollisionForce(ball, 0.5);
        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void lowBarrierCollisionRecordsOverlap() {
        Ball ball = createBall(1, 0, 0);
        SurroundingWalls.addLowBarrierCollisionForce(ball, -0.5);
        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void highBarrierCollisionRecordsOverlap() {
        Ball ball = createBall(1, 0, 0);
        SurroundingWalls.addHighBarrierCollisionForce(ball, 0.5);
        assertEquals(0.5, ball.getRecentTotalOverlap(), 0);
    }

    @Test
    public void testCalcLowXWallCollisionForce() {
        Ball ball = createBall(1, 0, 0);
        checkAddLeftBarrierCollisionForce(ball, 5, 0, 0); // no contact
        checkAddLeftBarrierCollisionForce(ball, 5, 4, 0); // just touching
        checkAddLeftBarrierCollisionForce(ball, 5, 4.5, 0.5); // overlap by 0.5
    }

    private void checkAddLeftBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);
        SurroundingWalls.addLeftBarrierCollisionForce(ball, wallX);
        assertNetForce(expected, (double) 0, ball.getEnvironment());
    }

    @Test
    public void testCalcHighXWallCollisionForce() {
        Ball ball = createBall(1, 0, 0);
        checkAddRightBarrierCollisionForce(ball, 5, 10, 0); // no contact
        checkAddRightBarrierCollisionForce(ball, 5, 6, 0); // just touching
        checkAddRightBarrierCollisionForce(ball, 5, 5.5, -0.5); // overlap by 0.5
    }

    private void checkAddRightBarrierCollisionForce(Ball ball, double ballX, double wallX, double expected) {
        ball.setCenterPosition(ballX, 0);
        SurroundingWalls.addRightBarrierCollisionForce(ball, wallX);
        assertNetForce(expected, (double) 0, ball.getEnvironment());
    }

    @Test
    public void testCalcLowYWallCollisionForce() {
        Ball ball = createBall(1, 0, 0);
        checkAddLowBarrierCollisionForce(ball, -5, -10, 0); // no contact
        checkAddLowBarrierCollisionForce(ball, -5, -6, 0); // just touching
        checkAddLowBarrierCollisionForce(ball, -5, -5.5, 0.5); // overlap by 0.5
    }

    private void checkAddLowBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);
        SurroundingWalls.addLowBarrierCollisionForce(ball, wallY);
        assertNetForce((double) 0, expected, ball.getEnvironment());
    }

    @Test
    public void testCalcHighYWallCollisionForce() {
        Ball ball = createBall(1, 0, 0);
        checkAddHighBarrierCollisionForce(ball, -5, 0, 0); // no contact
        checkAddHighBarrierCollisionForce(ball, -5, -4, 0); // just touching
        checkAddHighBarrierCollisionForce(ball, -5, -4.5, -0.5); // overlap by 0.5
    }

    private void checkAddHighBarrierCollisionForce(Ball ball, double ballY, double wallY, double expected) {
        ball.setCenterPosition(0, ballY);
        SurroundingWalls.addHighBarrierCollisionForce(ball, wallY);
        assertNetForce((double) 0, expected, ball.getEnvironment());
    }

    private Ball createBall(double radius, double centerX, double centerY) {
        Ball ball = new BallWithEnvironment();
        ball.setRadius(radius);
        ball.setCenterPosition(centerX, centerY);
        return ball;
    }
}
