package fga.evo.model.physics;

public class BallWithEnvironment extends Ball {
    NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();

    @Override
    public NewtonianBodyEnvironment getEnvironment() {
        return environment;
    }
}
