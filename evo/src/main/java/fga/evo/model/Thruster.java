package fga.evo.model;

public interface Thruster {
    //    static final Vector2D ZERO_THRUST = new Vector2D();
    //    static final Thruster ZERO_THRUSTER = new Thruster() {
    //        public Vector2D getThrust() {
    //            return ZERO_THRUST;
    //        }
    //    };

    void tick();

    double getForceX();

    double getForceY();

    double getEnergy();
}
