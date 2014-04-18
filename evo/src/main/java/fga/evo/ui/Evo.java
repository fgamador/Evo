package fga.evo.ui;

import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import fga.evo.model.Cell;
import fga.evo.model.World;

public class Evo extends Application {
    private World world;
    private Group cellCircles;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        world = new World(2000, 1000);
        world.populate(5, 10, 10);
        //world.populate(50, 10, 15);
        //        world = new World(200, 200);
        //        world.populate(2, 20);

        Group root = new Group();
        Scene scene = new Scene(root, world.getWidth(), world.getHeight(), Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Evo");

        Rectangle water = new Rectangle(scene.getWidth(), scene.getHeight(), //
            new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(0, Color.web("#111199")), new Stop(1, Color.BLACK)
            }));
        root.getChildren().add(water);

        cellCircles = new Group();
        for (Cell cell : world.getCells())
            cellCircles.getChildren().add(new CellCircle(cell));
        root.getChildren().add(cellCircles);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Set<Cell> newCells = world.tick();
                for (Cell cell : newCells)
                    cellCircles.getChildren().add(new CellCircle(cell));
                for (Node circle : cellCircles.getChildren())
                    ((CellCircle) circle).update();
            }
        });
        timeline.getKeyFrames().add(kf);
        timeline.play();

        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
