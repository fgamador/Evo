package fga.evo.model;

import fga.evo.model.physics.Ball;
import fga.evo.model.physics.BallWithEnvironment;
import fga.evo.model.physics.PairBond;

public class BondedPairMain {
    private static final int TICKS = 100;
    private static final int SUBTICKS_PER_TICK = 1;

    private static Ball ball1, ball2;
    private static PairBond bond;

    public static void main(String[] args) {
        createBondedPair();
        printHeader();
        for (int i = 0; i < TICKS; ++i)
            tick(i);
    }

    private static void createBondedPair() {
        ball1 = createBall(0);
        ball2 = createBall(21);
        bond = ball1.addBond(ball2);
    }

    private static Ball createBall(int centerX) {
        Ball ball1 = new BallWithEnvironment();
        ball1.setRadius(10);
        ball1.setMass(10);
        ball1.setCenterPosition(centerX, 0);
        return ball1;
    }

    private static void tick(int number) {
        for (int i = 0; i < SUBTICKS_PER_TICK; i++) {
            bond.addForces();
            printState(number, i);
            ball1.move(SUBTICKS_PER_TICK);
            ball2.move(SUBTICKS_PER_TICK);
        }
    }

    private static void printHeader() {
        System.out.println("Tick,Subtick,Ball1CenterX,Ball2CenterX,Ball1VelocityX,Ball2VelocityX,Ball1ForceX,Ball2ForceX");
    }

    private static void printState(int tick, int subtick) {
        System.out.println(tick + "," + subtick + "," //
                + ball1.getCenterX() + "," + ball2.getCenterX() + "," //
                + ball1.getVelocityX() + "," + ball2.getVelocityX() + "," //
                + ball1.getNetForceX() + "," + ball2.getNetForceX());
    }
}
