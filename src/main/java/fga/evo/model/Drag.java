package fga.evo.model;

import static fga.evo.model.Util.sqr;

/**
 * Drag caused by the fluid medium in which the cells live.
 */
public class Drag extends EnvironmentalInfluence {
    static DoubleParameter dragFactor = new DoubleParameter(0.001);

    @Override
    public void addForcesToCell(Ball cell) {
        // TODO if the cell is not wholly submerged, drag will be reduced, but it depends on what direction the cell is moving
        double dragX = calcDrag(cell.getVelocityX(), cell.getRadius());
        double dragY = calcDrag(cell.getVelocityY(), cell.getRadius());
        cell.addForce(dragX, dragY);
    }

    private double calcDrag(double velocity, double radius) {
        return -Math.signum(velocity) * dragFactor.getValue() * radius * sqr(velocity);
    }

    // From old version with circulating current. Could be in a new DragWithCurrent subclass.
//    private void fluidResistanceForce() {
//        double relativeVelocityX = velocityX - world.getCurrentX(centerX, centerY);
//        double relativeVelocityY = velocityY - world.getCurrentY(centerX, centerY);
//        double dragX = -Math.signum(relativeVelocityX) * World.FLUID_RESISTANCE * radius
//                * sqr(relativeVelocityX);
//        double dragY = -Math.signum(relativeVelocityY) * World.FLUID_RESISTANCE * radius
//                * sqr(relativeVelocityY);
//        forceX += dragX;
//        forceY += dragY;
//        if (forces != null) {
//            forces.add(new Force(0, 0, dragX, dragY));
//        }
//    }
//
//    /** Returns the horizontal strength of the current at a specified point. */
//    final double getCurrentX(double x, double y) {
//        return Math.sin(2 * Math.PI * y / height) * Math.sin(Math.PI * x / width);
//    }
//
//    /** Returns the vertical strength of the current at a specified point. */
//    final double getCurrentY(double x, double y) {
//        return -Math.sin(2 * Math.PI * x / width) * Math.sin(Math.PI * y / height);
//    }
}
