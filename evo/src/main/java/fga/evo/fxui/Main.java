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
    public static final int WIDTH = 500;
    public static final int AIR_HEIGHT = 50;
    public static final int WATER_DEPTH = 500;

    private World world;
    private Group cellCircles;
    private Timeline timeline;
//    private CellCircle selectedCellCircle;

    @Override
    public void start(Stage primaryStage) throws IOException {
        world = new World();
        addInfluences();
        populate();

        createMainWindow(primaryStage);

//        ControlWindow controls = new ControlWindow(primaryStage, this);
//        controls.show();
    }

    private void addInfluences() {
        world.addEnvironmentalInfluence(new SurroundingWalls(0, WIDTH, -WATER_DEPTH, AIR_HEIGHT));
        world.addEnvironmentalInfluence(new Drag());
        world.addEnvironmentalInfluence(new Weight());
        world.addEnvironmentalInfluence(new Illumination(WATER_DEPTH));
    }

    private void populate() {
//        Cell cell = new Cell(10, new FixedDepthSeekingControl(0));
//        Cell cell = new Cell(10, new DuckweedControl());
//        cell.setCenterPosition(WIDTH / 2, -WATER_DEPTH / 2);
//        world.addCell(cell);

        World.setSubticksPerTick(2);

        Cell cell1 = new Cell(20, new DuckweedControl());
        world.addCell(cell1);

        Cell cell2 = new Cell(1, c -> c.requestPhotoAreaResize(1));
        world.addCell(cell2);
        cell1.addBond(cell2);

        Cell cell3 = new Cell(1, c -> c.requestPhotoAreaResize(1));
        world.addCell(cell3);
        cell1.addBond(cell3);

        cell1.setCenterPosition(250, -250);
        cell2.setCenterPosition(270, -250);
        cell3.setCenterPosition(230, -250);
    }

    private void createMainWindow(Stage primaryStage) {
        Group root = createSceneRoot(primaryStage);
        addAirRectangle(root);
        addWaterRectangle(root);
        addCellCircles(root);
        addMouseListeners(root);
        startAnimation();
        primaryStage.show();
    }

    private Group createSceneRoot(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, AIR_HEIGHT + WATER_DEPTH, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Evo");
        return root;
    }

    private void addAirRectangle(Group root) {
        Rectangle air = new Rectangle(WIDTH, AIR_HEIGHT,
            new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(0.75, 0.9, 1)),
                new Stop(1, Color.color(0.9, 0.98, 1))));
        root.getChildren().add(air);
    }

    private void addWaterRectangle(Group root) {
        Rectangle water = new Rectangle(WIDTH, WATER_DEPTH,
            new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(0.1, 0.26, 0.55)), // Color.web("#111199")),
                new Stop(1, Color.BLACK)));
        water.setY(toSceneY(0));
        root.getChildren().add(water);
    }

    private void addCellCircles(Group root) {
        cellCircles = new Group();
        for (Cell cell : world.getCells()) {
            addCell(cell);
        }
        root.getChildren().add(cellCircles);
    }

    private void addMouseListeners(Group root) {
        root.setOnMouseDragged(this::onMouseDragged);
//        root.setOnMouseDragged(e -> onMouseDragged(e));
        root.setOnMouseReleased(e -> onMouseReleased());
    }

    private void startAnimation() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(40), e -> tick());
        timeline.getKeyFrames().add(kf);
        timeline.play();
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
        world.setPullPoint(toWorldX(e.getSceneX()), toWorldY(e.getSceneY()));
//        if (pulledCellCircle != selectedCellCircle) {
//            onCellClicked(cellCircle);
//        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (world.isPulling()) {
            world.setPullPoint(toWorldX(e.getSceneX()), toWorldY(e.getSceneY()));
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

    public static double toSceneX(final double worldX) {
        return worldX;
    }

    public static double toWorldX(final double sceneX) {
        return sceneX;
    }

    public static double toSceneY(final double worldY) {
        return AIR_HEIGHT - worldY;
    }

    public static double toWorldY(final double sceneY) {
        return AIR_HEIGHT - sceneY;
    }

    // -- main --

    public static void main(String[] args) {
        launch(args);
    }
}
