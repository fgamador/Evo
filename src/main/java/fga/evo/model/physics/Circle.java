package fga.evo.model.physics;

public interface Circle {
    double getCenterX();

    double getCenterY();

    double getRadius();

    void onPossibleOverlap(Circle circle);
}
