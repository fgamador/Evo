package fga.evo.fxui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ControlDialogController {
    protected Evo evo;

    @FXML
    private Button playPauseButton;
    @FXML
    private Button singleStepButton;

    public void setEvo(Evo val) {
        evo = val;
    }

    @FXML
    private void onPlayPauseButtonClicked() {
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

    @FXML
    private void onSingleStepButtonClicked() {
        evo.tick();
    }
}
