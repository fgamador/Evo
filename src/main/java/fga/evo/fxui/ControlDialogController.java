package fga.evo.fxui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ControlDialogController {
    private Image playIcon;
    private Image pauseIcon;
    private Evo evo;

    @FXML
    private Button playOrPauseButton;
    @FXML
    private Button singleStepButton;
    @FXML
    private Button restartButton;

    @FXML
    private void initialize() {
        playIcon = new Image(getClass().getResourceAsStream("4_audio_play.png"));
        pauseIcon = new Image(getClass().getResourceAsStream("4_audio_pause.png"));
        //playButton.managedProperty().bind(playButton.visibleProperty());
        //pauseButton.managedProperty().bind(pauseButton.visibleProperty());
    }

    public void setEvo(Evo val) {
        evo = val;
        updatePerEvoState();
    }

    @FXML
    private void onPlayOrPauseButtonClicked() {
        if (evo.isRunning()) {
            evo.pause();
        } else {
            evo.resume();
        }
        updatePerEvoState();
    }

    @FXML
    private void onSingleStepButtonClicked() {
        evo.tick();
    }

    @FXML
    private void onRestartButtonClicked() {
        // TODO confirm
        evo.restart();
    }

    private void updatePerEvoState() {
        playOrPauseButton.setGraphic(new ImageView(evo.isRunning() ? pauseIcon : playIcon));
        singleStepButton.setDisable(evo.isRunning());
        restartButton.setDisable(evo.isRunning());
    }
}
