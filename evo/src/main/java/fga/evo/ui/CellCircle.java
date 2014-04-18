package fga.evo.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import fga.evo.model.Cell;

public class CellCircle extends Group {
    private Cell cell;
    private Circle phRingCircle;
    private Circle fatCircle;
    private boolean wasAlive = true;

    CellCircle(Cell cell) {
        this.cell = cell;

        Color phRingColor = Color.web("#99ff00", 0.7);
        phRingCircle = new Circle(cell.getPhotosyntheticRingOuterRadius(), phRingColor);
        //        phRingCircle.setStrokeType(StrokeType.INSIDE);
        //        phRingCircle.setStroke(Color.web("#99FF00", 0.5));
        //        phRingCircle.setStrokeWidth(2);
        getChildren().add(phRingCircle);

        Color fatCircleColor = Color.web("#ddff00", 1);
        fatCircle = new Circle(cell.getFatRadius(), fatCircleColor);
        getChildren().add(fatCircle);

        setTranslateX(cell.getCenterX());
        setTranslateY(cell.getCenterY());
    }

    void update() {
        setTranslateX(cell.getCenterX());
        setTranslateY(cell.getCenterY());

        phRingCircle.setRadius(cell.getPhotosyntheticRingOuterRadius());
        fatCircle.setRadius(cell.getFatRadius());

        if (wasAlive && !cell.isAlive()) {
            wasAlive = false;
            phRingCircle.setFill(Color.web("#777777", 0.7));
            fatCircle.setFill(Color.web("#aaaaaa", 0.8));
        }
    }

    Cell getCell() {
        return cell;
    }
}
