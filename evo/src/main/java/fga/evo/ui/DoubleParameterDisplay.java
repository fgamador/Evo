package fga.evo.ui;

import javafx.beans.property.adapter.JavaBeanDoubleProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import fga.evo.model.ParameterAccess;

public class DoubleParameterDisplay extends Group {
    private String name;
    private Text text;
    private TextField field;

    public DoubleParameterDisplay() {
        text = new Text();
        text.setOnMouseClicked(e -> onTextClicked());
        getChildren().add(text);

        field = new TextField();
        field.setVisible(false);
        field.setOnAction(e -> onValueEntered());
        getChildren().add(field);
    }

    public void setName(String value) {
        name = value;
        onSetName();
    }

    public String getName() {
        return name;
    }

    private void onSetName() {
        try {
            JavaBeanDoublePropertyBuilder builder = JavaBeanDoublePropertyBuilder.create();
            builder.bean(ParameterAccess.get());
            builder.name(name);
            JavaBeanDoubleProperty property = builder.build();
            text.textProperty().bind(field.textProperty());
            field.textProperty().bindBidirectional(property, new NumberStringConverter());
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
        text.setVisible(true);
        field.setVisible(false);
    }
}
