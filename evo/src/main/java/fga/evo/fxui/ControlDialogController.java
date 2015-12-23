package fga.evo.fxui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ControlDialogController {
    private Evo evo;

    @FXML
    private Button playPauseButton;
    @FXML
    private Button singleStepButton;

//    public ControlDialogController(Evo evo) {
//        this.evo = evo;
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ControlPane.fxml"));
//        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
//
//        try {
//            fxmlLoader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
