package fga.evo.model.geometry;

public interface OverlappableCircle extends Circle {
    void onOverlap(OverlappableCircle circle, double overlap);
}
