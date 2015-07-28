package fga.evo.model;

/**
 * The fluid medium in which the cells live.
 *
 * @author Franz Amador
 */
public class Fluid extends EnvironmentComponent {
    private static double dragFactor = 0.001;

    @Override
    public void addForcesToCell(final Cell cell) {
        double dragX = -Math.signum(cell.getVelocityX()) * dragFactor * cell.getRadius()
                * sqr(cell.getVelocityX());
        double dragY = -Math.signum(cell.getVelocityY()) * dragFactor * cell.getRadius()
                * sqr(cell.getVelocityY());
        cell.addForce(dragX, dragY);
    }

    // from old version
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

    private static double sqr(final double value) {
        return value * value;
    }

    //=========================================================================
    // Parameters
    //=========================================================================

    public static double getDragFactor() {
        return dragFactor;
    }

    public static void setDragFactor(final double val) {
        dragFactor = val;
    }
}
