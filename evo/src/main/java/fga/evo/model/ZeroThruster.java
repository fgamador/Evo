package fga.evo.model;

public final class ZeroThruster implements Thruster {
    public static final Thruster INSTANCE = new ZeroThruster();

    private ZeroThruster() {
    }

    public void tick() {
    }

    public double getForceX() {
        return 0;
    }

    public double getForceY() {
        return 0;
    }

    public double getEnergy() {
        return 0;
    }
}
