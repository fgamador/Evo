package fga.evo.model.physics;

public interface Circle {
    double getRadius();

    void onPossibleOverlap(Circle circle);
}
