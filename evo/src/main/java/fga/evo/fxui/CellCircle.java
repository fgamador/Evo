package fga.evo.fxui;

import fga.evo.model.Cell;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

/**
 * A circle that represents a cell.
 *
 * @author Franz Amador
 */
public class CellCircle extends Group {
    private Cell cell;
    private Circle photoRingCircle;
//    private Circle fatCircle;
    private boolean wasAlive = true;
    private boolean selected;
//    private List<ForceArrow> forceArrows = new ArrayList<>();

    CellCircle(Cell cell) {
        this.cell = cell;

        Color photoRingColor = Color.web("#99ff00", 0.7);
//        photoRingCircle = new Circle(cell.getPhotosyntheticRingOuterRadius(), photoRingColor);
        photoRingCircle = new Circle(cell.getRadius(), photoRingColor);
        getChildren().add(photoRingCircle);

//        Color fatCircleColor = Color.web("#ddff00", 1);
//        fatCircle = new Circle(cell.getFatRadius(), fatCircleColor);
//        getChildren().add(fatCircle);

        setTranslateX(cell.getCenterX());
        setTranslateY(cell.getCenterY());
    }

    void update() {
        setTranslateX(cell.getCenterX());
        setTranslateY(cell.getCenterY());

//        photoRingCircle.setRadius(cell.getPhotosyntheticRingOuterRadius());
//        fatCircle.setRadius(cell.getFatRadius());

//        if (wasAlive && !cell.isAlive()) {
//            wasAlive = false;
//            photoRingCircle.setFill(Color.web("#777777", 0.7));
//            fatCircle.setFill(Color.web("#aaaaaa", 0.8));
//        }

//        if (selected) {
//            getChildren().removeAll(forceArrows);
//            forceArrows.clear();
//            for (Force force : cell.getForces()) {
//                ForceArrow arrow = new ForceArrow(force);
//                forceArrows.add(arrow);
//                getChildren().add(arrow);
//            }
//        }
    }

    Cell getCell() {
        return cell;
    }

//    void setSelected(boolean val) {
//        selected = val;
//        if (selected) {
//            photoRingCircle.setStrokeType(StrokeType.OUTSIDE);
//            photoRingCircle.setStroke(Color.web("white"));
//            photoRingCircle.setStrokeWidth(2);
//            cell.setRecordForces(true);
//        } else {
//            photoRingCircle.setStroke(null);
//            cell.setRecordForces(false);
//            getChildren().removeAll(forceArrows);
//            forceArrows.clear();
//        }
//    }
}

