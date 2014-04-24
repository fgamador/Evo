package fga.evo.ui;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControlWindow extends Stage {
    //private Cell selectedCell;

    ControlWindow(Stage primaryStage, Evo evo) throws IOException {
        initOwner(primaryStage);
        //initStyle(StageStyle.UTILITY);

        ControlPane root = new ControlPane(evo);
        Scene scene = new Scene(root, 300, 275);
        setScene(scene);
        setTitle("Evo Controls");
    }

    //    void setSelectedCell(Cell value) {
    //        selectedCell = value;
    //    }
}
