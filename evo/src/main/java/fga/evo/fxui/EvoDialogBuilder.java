package fga.evo.fxui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvoDialogBuilder {
    private Evo evo;
    private Stage dialogStage;
    private FXMLLoader loader;

    public EvoDialogBuilder(Stage primaryStage, Evo evo) {
        this.evo = evo;
        dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);
    }

    public EvoDialogBuilder setTitle(String title) {
        dialogStage.setTitle(title);
        return this;
    }

    public EvoDialogBuilder setModality(Modality modality) {
        dialogStage.initModality(modality);
        return this;
    }

    public EvoDialogBuilder setFxmlFileName(String fxmlFileName) {
        loadFxmlFile(fxmlFileName);
        createScene();
        return this;
    }

    private void loadFxmlFile(String fxmlFileName) {
        loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createScene() {
        Pane dialog = loader.getRoot();
        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);
    }

    public void show() {
        EvoDialogController controller = loader.getController();
        //controller.setDialogStage(dialogStage);
        controller.setEvo(evo);
        dialogStage.show();
    }
}
