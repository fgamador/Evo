package fga.evo.fxui;

import javafx.beans.property.adapter.JavaBeanDoubleProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import fga.evo.model.ParameterAccess;

public class EditableDoubleParameter extends Group {
    private Text text;
    private TextField field;
    private JavaBeanDoubleProperty parameter;

    public EditableDoubleParameter() {
        text = new Text();
        text.setOnMouseClicked(e -> onTextClicked());
        getChildren().add(text);

        field = new TextField();
        field.setVisible(false);
        field.setOnAction(e -> onValueEntered());
        getChildren().add(field);

        text.textProperty().bind(field.textProperty());
    }

    public String getName() {
        return parameter.getName();
    }

    public void setName(String value) {
        createParameterProperty(value);
        field.setText(String.valueOf(parameter.getValue()));
        //field.textProperty().bindBidirectional(parameter, new NumberStringConverter());
    }

    private void createParameterProperty(String name) {
        try {
            JavaBeanDoublePropertyBuilder builder = JavaBeanDoublePropertyBuilder.create();
            builder.bean(ParameterAccess.get());
            builder.name(name);
            parameter = builder.build();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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
