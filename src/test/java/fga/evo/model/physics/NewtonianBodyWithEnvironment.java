package fga.evo.model.physics;

class NewtonianBodyWithEnvironment extends NewtonianBody {
    NewtonianBodyEnvironment environment = new NewtonianBodyEnvironment();

    public NewtonianBodyWithEnvironment(double centerX, double centerY, double velocityX, double velocityY) {
        super(centerX, centerY, velocityX, velocityY);
    }

    @Override
    public NewtonianBodyEnvironment getEnvironment() {
        return environment;
    }
}