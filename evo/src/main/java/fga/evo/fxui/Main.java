package fga.evo.fxui;

import fga.evo.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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

import java.io.IOException;

/**
 * Evo's UI application class.
 */
public class Main extends Application {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private World world;
    private Group cellCircles;
    private Timeline timeline;
//    private CellCircle selectedCellCircle;

    @Override
    public void start(Stage primaryStage) throws IOException {
        world = new World();
        world.addEnvironmentalInfluence(new Walls(WIDTH, HEIGHT));
        world.addEnvironmentalInfluence(new Drag());
        world.addEnvironmentalInfluence(new Buoyancy());
        populate();

        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Evo");

        Rectangle water = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#111199")), new Stop(1, Color.BLACK)));
        root.getChildren().add(water);

        cellCircles = new Group();
        for (Cell cell : world.getCells()) {
            addCell(cell);
        }
        root.getChildren().add(cellCircles);

        root.setOnMouseDragged(this::onMouseDragged);
//        root.setOnMouseDragged(e -> onMouseDragged(e));
        root.setOnMouseReleased(e -> onMouseReleased());

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(40), e -> tick());
        timeline.getKeyFrames().add(kf);
        timeline.play();

        primaryStage.show();

//        ControlWindow controls = new ControlWindow(primaryStage, this);
//        controls.show();
    }

    private void populate() {
        Cell cell = new Cell(10);
        cell.setPosition(250, 250);
        world.addCell(cell);

//        Cell cell2 = new Cell(10);
//        cell2.setPosition(230, 250);
//        world.addCell(cell2);
//
//        Cell cell3 = new Cell(10);
//        cell3.setPosition(210, 250);
//        world.addCell(cell3);
//
//        cell.addBond(cell2);
//        cell2.addBond(cell3);
//
//        Cell cell4 = new Cell(10);
//        cell4.setPhysics(2);
//        cell4.setPosition(100, 100);
//        world.addCell(cell4);
//
//        Cell cell5 = new Cell(10);
//        cell5.setPhysics(2);
//        cell5.setPosition(80, 100);
//        world.addCell(cell5);
//
//        Cell cell6 = new Cell(10);
//        cell6.setPhysics(2);
//        cell6.setPosition(60, 100);
//        world.addCell(cell6);
//
//        cell4.addBond(cell5);
//        cell5.addBond(cell6);
    }

    void tick() {
        world.tick();
//        Set<Cell> newCells = world.tick();
//        for (Cell cell : newCells) {
//            addCell(cell);
//        }
        for (Node circle : cellCircles.getChildren()) {
            ((CellCircle) circle).update();
        }
    }

    private void addCell(Cell cell) {
        CellCircle cellCircle = new CellCircle(cell);
        cellCircles.getChildren().add(cellCircle);
        // TODO cellCircle.setOnMouseClicked(e -> onCellClicked(cellCircle));
        cellCircle.setOnMousePressed(e -> onCellPressed(cellCircle, e));
    }

    // -- event handlers --

//    private void onCellClicked(CellCircle cellCircle) {
//        if (selectedCellCircle != null) {
//            selectedCellCircle.setSelected(false);
//        }
//        if (selectedCellCircle == cellCircle) {
//            selectedCellCircle = null;
//        } else {
//            cellCircle.setSelected(true);
//            selectedCellCircle = cellCircle;
//        }
//    }

    private void onCellPressed(CellCircle cellCircle, MouseEvent e) {
        world.startPull(cellCircle.getCell());
        world.setPullPoint(e.getSceneX(), e.getSceneY());
//        if (pulledCellCircle != selectedCellCircle) {
//            onCellClicked(cellCircle);
//        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (world.isPulling()) {
            world.setPullPoint(e.getSceneX(), e.getSceneY());
        }
    }

    private void onMouseReleased() {
        if (world.isPulling()) {
            world.endPull();
        }
    }

    // -- for control window --

//    boolean isRunning() {
//        return timeline.getStatus().equals(Animation.Status.RUNNING);
//    }
//
//    void pause() {
//        timeline.pause();
//    }
//
//    void resume() {
//        timeline.play();
//    }

    // -- main --

    public static void main(String[] args) {
        launch(args);
    }
}
