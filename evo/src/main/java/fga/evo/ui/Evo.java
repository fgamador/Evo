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
import javafx.scene.input.MouseEvent;
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
    private CellCircle selectedCellCircle;
    private CellCircle pulledCellCircle;

    @Override
    public void start(Stage stage) {
        //        world = new World(2000, 1000);
        //        world.populate(5, 10, 10);
        //world.populate(50, 10, 15);
        //        world = new World(200, 200);
        //        world.populate(2, 10, 20);
        world = new World(500, 500);
        world.populate(1, 10, 10);

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
        for (Cell cell : world.getCells()) {
            addCell(cell);
        }
        root.getChildren().add(cellCircles);

        root.setOnMouseDragged(e -> onMouseDragged(e));
        root.setOnMouseReleased(e -> onMouseReleased());

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Set<Cell> newCells = world.tick();
                for (Cell cell : newCells) {
                    addCell(cell);
                }
                for (Node circle : cellCircles.getChildren()) {
                    ((CellCircle) circle).update();
                }
            }
        });
        timeline.getKeyFrames().add(kf);
        timeline.play();

        stage.show();
    }

    private void addCell(Cell cell) {
        CellCircle cellCircle = new CellCircle(cell);
        cellCircles.getChildren().add(cellCircle);
        // TODO cellCircle.setOnMouseClicked(e -> onCellClicked(cellCircle));
        cellCircle.setOnMousePressed(e -> onCellPressed(cellCircle, e));
    }

    private void onCellClicked(CellCircle cellCircle) {
        if (selectedCellCircle != null) {
            selectedCellCircle.setSelected(false);
        }
        if (selectedCellCircle == cellCircle) {
            selectedCellCircle = null;
        } else {
            cellCircle.setSelected(true);
            selectedCellCircle = cellCircle;
        }
    }

    private void onCellPressed(CellCircle cellCircle, MouseEvent e) {
        pulledCellCircle = cellCircle;
        pulledCellCircle.setPullPoint(e.getSceneX(), e.getSceneY());
        if (pulledCellCircle != selectedCellCircle) {
            onCellClicked(cellCircle);
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (pulledCellCircle != null) {
            pulledCellCircle.setPullPoint(e.getSceneX(), e.getSceneY());
        }
    }

    private void onMouseReleased() {
        if (pulledCellCircle != null) {
            pulledCellCircle.releasePull();
            pulledCellCircle = null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
