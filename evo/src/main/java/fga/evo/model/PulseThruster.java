package fga.evo.model;

public class PulseThruster implements Thruster {
    private double thrustingForceX, thrustingForceY;
    private double forceX, forceY;
    private int thrustTicks;
    private int cycleTicks;
    private int ticks = 0;

    public PulseThruster(double forceX, double forceY, int thrustTicks, int cycleTicks) {
        this.thrustingForceX = forceX;
        this.thrustingForceY = forceY;
        this.thrustTicks = thrustTicks;
        this.cycleTicks = cycleTicks;
    }

    public void tick() {
        if (ticks++ < thrustTicks) {
            forceX = thrustingForceX;
            forceY = thrustingForceY;
        } else {
            forceX = 0;
            forceY = 0;
        }

        if (ticks++ > cycleTicks)
            ticks = 0;
    }

    public double getForceX() {
        return forceX;
    }

    public double getForceY() {
        return forceY;
    }

    public double getEnergy() {
        // TODO Auto-generated method stub
        return 0;
    }
}
