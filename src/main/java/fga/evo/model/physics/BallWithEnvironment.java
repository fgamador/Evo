package fga.evo.model.physics;

public class BallWithEnvironment extends Ball {
    private BallEnvironment environment = new BallEnvironment();

    @Override
    public BallEnvironment getEnvironment() {
        return environment;
    }
}
