package fga.evo.model;

public final class ParameterAccess {
    private static ParameterAccess instance = new ParameterAccess();

    private ParameterAccess() {
    }

    public static ParameterAccess get() {
        return instance;
    }

    public double getBondSpringConstant() {
        return CellCellBond.SPRING_CONSTANT;
    }

    public void setBondSpringConstant(double value) {
        CellCellBond.SPRING_CONSTANT = value;
    }

    public double getBondDampingConstant() {
        return CellCellBond.DAMPING_CONSTANT;
    }

    public void setBondDampingConstant(double value) {
        CellCellBond.DAMPING_CONSTANT = value;
    }
}
