package fga.evo.model;

public final class ParameterAccess {
    private static ParameterAccess instance = new ParameterAccess();

    private ParameterAccess() {
    }

    public static ParameterAccess get() {
        return instance;
    }

    public double getBondSpringConstant() {
        return BallForces.getOverlapForceFactor();
    }

    public void setBondSpringConstant(double value) {
        BallForces.setOverlapForceFactor(value);
    }

    public double getBondDampingConstant() {
        return BallForces.getDampingForceFactor();
    }

    public void setBondDampingConstant(double value) {
        BallForces.setDampingForceFactor(value);
    }

    public double getMaxSpeed() {
        return Ball.getSpeedLimit();
    }

    public void setMaxSpeed(double value) {
        Ball.setSpeedLimit(value);
    }

//    public double getUsableGrowthEnergyPerMass() {
//        return Cell.USABLE_GROWTH_ENERGY_PER_MASS;
//    }
//
//    public void setUsableGrowthEnergyPerMass(double value) {
//        Cell.USABLE_GROWTH_ENERGY_PER_MASS = value;
//    }
}
