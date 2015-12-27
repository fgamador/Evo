package fga.evo.fxui;

import fga.evo.model.Cell;
import fga.evo.model.World;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collection;

/**
 * Evo's UI application class.
 */
public abstract class Evo extends Application {
    public static final int WIDTH = 1000;
    public static final int AIR_HEIGHT = 50;
    public static final int WATER_DEPTH = 500;

    private World world;
    private Group cellCircles;
    private Timeline timeline;
    private ContextMenu contextMenu;
//    private CellCircle selectedCellCircle;

    @Override
    public void start(Stage primaryStage) throws IOException {
        world = new World();
        addInfluences(world);
        populate(world);
        createMainWindow(primaryStage);
    }

    protected abstract void addInfluences(World world);

    protected abstract void populate(World world);

    private void createMainWindow(Stage primaryStage) {
        Group root = createSceneRoot(primaryStage);
        createContextMenu(primaryStage);
        addAirRectangle(root);
        addWaterRectangle(root);
        addCellCircles(root);
        addMouseListeners(root);
        addResizeListeners(primaryStage);
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

    private void createContextMenu(Stage primaryStage) {
        contextMenu = new ContextMenu();

        final MenuItem timeItem = new MenuItem("Time...");
        timeItem.setOnAction(e -> {
            double menuX = timeItem.getParentPopup().getX();
            double menuY = timeItem.getParentPopup().getY();
            showControlDialog(primaryStage, menuX + 10, menuY + 5);
        });

        final MenuItem parametersItem = new MenuItem("Parameters...");
        parametersItem.setOnAction(e -> {
            double menuX = parametersItem.getParentPopup().getX();
            double menuY = parametersItem.getParentPopup().getY();
            showParametersDialog(primaryStage, menuX + 10, menuY + 25);
        });

        contextMenu.getItems().addAll(timeItem, parametersItem);
    }

    private void showControlDialog(Stage primaryStage, double screenX, double screenY) {
        DialogBuilder builder = new DialogBuilder("ControlDialog.fxml")
                .setParent(primaryStage)
                .setTitle("Time")
                .setModality(Modality.NONE)
                .setLocation(screenX, screenY);

        ControlDialogController controller = builder.getController();
        controller.setEvo(this);

        builder.show();
    }

    private void showParametersDialog(Stage primaryStage, double screenX, double screenY) {
        new DialogBuilder("ParametersDialog.fxml")
                .setParent(primaryStage)
                .setTitle("Parameters")
                .setModality(Modality.NONE)
                .setLocation(screenX, screenY)
                .setWidth(270)
                .show();
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

    private void addMouseListeners(final Group root) {
        root.setOnContextMenuRequested(e -> {
            contextMenu.show(root, e.getScreenX(), e.getScreenY());
            e.consume();
        });
        root.setOnMousePressed(e -> {
            contextMenu.hide();
            if (e.getButton() == MouseButton.MIDDLE) {
                addCell(e.getSceneX(), e.getSceneY());
            }
        });

        root.setOnMouseDragged(e -> {
            if (world.isPulling()) {
                world.setPullPoint(toWorldX(e.getSceneX()), toWorldY(e.getSceneY()));
            }
        });
        root.setOnMouseReleased(e -> {
            if (world.isPulling()) {
                world.endPull();
            }
        });
    }

    private void addCell(double sceneX, double sceneY) {
        Cell cell = createCell();
        cell.setCenterPosition(toWorldX(sceneX), toWorldY(sceneY));
        world.addCell(cell);
        addCell(cell);
    }

    protected abstract Cell createCell();

    private void addResizeListeners(Stage primaryStage) {
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            onWidthChanged(newWidth.doubleValue());
        });
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            onHeightChanged(newHeight.doubleValue());
        });
    }

    protected abstract void onWidthChanged(double newWidth);

    protected abstract void onHeightChanged(double newHeight);

    private void startAnimation() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(40), e -> tick());
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    void tick() {
        Collection<Cell> newCells = world.tick();
        for (Cell cell : newCells) {
            addCell(cell);
        }
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

    // -- for control window --

    boolean isRunning() {
        return timeline.getStatus().equals(Animation.Status.RUNNING);
    }

    void pause() {
        timeline.pause();
    }

    void resume() {
        timeline.play();
    }

    public void restart() {
        for (Node cellCircle : cellCircles.getChildren()) {
            cellCircle.setVisible(false);
        }
        cellCircles.getChildren().clear();
        world.restart();
    }

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
}
