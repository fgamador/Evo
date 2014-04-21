package fga.evo.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import fga.evo.model.Force;

public class ForceArrow extends Group {
    ForceArrow(Force force) {
        Circle originCircle = new Circle(force.getOriginX(), force.getOriginY(), 2, Color.web("red"));
        getChildren().add(originCircle);

        Line line = new Line(force.getOriginX(), force.getOriginY(), //
            force.getOriginX() + 10 * force.getForceX(), //
            force.getOriginY() + 10 * force.getForceY());
        line.setStroke(Color.web("red"));
        getChildren().add(line);
    }
}
