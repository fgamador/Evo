package fga.evo.fxui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public final class ControlPane extends BorderPane {
    private Evo evo;

    @FXML
    private Button playPauseButton;
    @FXML
    private Button singleStepButton;

    public ControlPane(Evo evo) {
        this.evo = evo;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControlPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
