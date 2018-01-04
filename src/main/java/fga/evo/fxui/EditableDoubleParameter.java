package fga.evo.fxui;

import fga.evo.model.util.DoubleParameter;
import fga.evo.model.RegisteredParameters;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditableDoubleParameter extends Group {
    private Text text;
    private TextField field;
    private DoubleParameter parameter;

    public EditableDoubleParameter() {
        text = new Text();
        text.setOnMouseClicked(e -> onTextClicked());
        getChildren().add(text);

        field = new TextField();
        field.setPrefWidth(60);
        field.setVisible(false);
        field.setOnAction(e -> onValueEntered());
        getChildren().add(field);

        text.textProperty().bind(field.textProperty());
    }

    // Probably not called, but needed for JavaFX to recognize 'name' as a bean property.
    public String getName() {
        return "";
    }

    public void setName(String name) {
        parameter = RegisteredParameters.get(name);
        if (parameter == null) {
            throw new IllegalArgumentException("No such parameter: " + name);
        }

        field.setText(String.valueOf(parameter.getValue()));
    }

    private void onTextClicked() {
        text.setVisible(false);
        field.setVisible(true);
        field.requestFocus();
    }

    private void onValueEntered() {
        text.setStyle("");
        text.setVisible(true);
        field.setVisible(false);
        try {
            parameter.setValue(Double.valueOf(field.getText()));
            field.setText(String.valueOf(parameter.getValue()));
        } catch (NumberFormatException e) {
            text.setStyle("-fx-fill: red;");
        }
    }
}
