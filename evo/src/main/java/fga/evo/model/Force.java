package fga.evo.model;

/**
 * Represents an instantaneous force. Use only when making forces available for
 * drawing as arrows in the UI.
 */
public final class Force {
    private double originX, originY;
    private double forceX, forceY;

    Force(double originX, double originY, double forceX, double forceY) {
        this.originX = originX;
        this.originY = originY;
        this.forceX = forceX;
        this.forceY = forceY;
    }

    public double getOriginX() {
        return originX;
    }

    public double getOriginY() {
        return originY;
    }

    public double getForceX() {
        return forceX;
    }

    public double getForceY() {
        return forceY;
    }
}
