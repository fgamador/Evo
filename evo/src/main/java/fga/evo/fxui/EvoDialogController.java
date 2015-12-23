package fga.evo.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class EvoDialogController {
    protected Evo evo;

    public void setEvo(Evo val) {
        evo = val;
    }
}
