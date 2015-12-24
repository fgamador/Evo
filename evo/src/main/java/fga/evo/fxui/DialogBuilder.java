package fga.evo.fxui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogBuilder {
    private Stage dialogStage;
    private FXMLLoader loader;

    public DialogBuilder(String fxmlFileName) {
        dialogStage = new Stage();
        loadFxmlFile(fxmlFileName);
        createScene();
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

    public DialogBuilder setParent(Stage parentStage) {
        dialogStage.initOwner(parentStage);
        return this;
    }

    public DialogBuilder setTitle(String title) {
        dialogStage.setTitle(title);
        return this;
    }

    public DialogBuilder setModality(Modality modality) {
        dialogStage.initModality(modality);
        return this;
    }

    public <T> T getController() {
        return loader.getController();
    }

    public void show() {
        dialogStage.show();
    }
}
