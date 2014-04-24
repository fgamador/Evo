package fga.evo.ui;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public final class ControlPane extends BorderPane {
    private Evo evo;

    @FXML
    private Button playPauseButton;
    @FXML
    private Button singleStepButton;

    @FXML
    private Text centerX;

    public ControlPane(Evo evo) {
        this.evo = evo;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControlPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void onPlayPauseButtonClicked() {
        if (evo.isRunning()) {
            evo.pause();
            playPauseButton.setText(">");
            singleStepButton.setDisable(false);
        } else {
            evo.resume();
            playPauseButton.setText("||");
            singleStepButton.setDisable(true);
        }
    }

    public void onSingleStepButtonClicked() {
        evo.tick();
    }

    public String getCenterX() {
        return centerXProperty().get();
    }

    public void setCenterX(String value) {
        centerXProperty().set(value);
    }

    public StringProperty centerXProperty() {
        return centerX.textProperty();
    }
}
