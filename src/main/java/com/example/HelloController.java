package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class HelloController {

//    Esta variable está vinculada al archivo FXML.
    @FXML
    private GridPane gridPane;

    private Button scenario1Button;

    @FXML
    public void initialize() {
        // Se crea el boton para Escenario 1
        scenario1Button = createScenarioButton("/escenario1/Fondo.png", "Escenario 1");

        // Se agrega botton de grifpane
        gridPane.add(scenario1Button, 0, 0);

        // Configuracion de boton de action
        scenario1Button.setOnAction(e -> selectScenario());
    }

    private Button createScenarioButton(String imagePath, String text) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        Button button = new Button(text, imageView);
        button.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        button.setPrefWidth(250);
        button.setPrefHeight(250);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: #cccccc; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-font-size: 18px;");
        return button;
    }

    private void selectScenario() {
        scenario1Button.setStyle("-fx-background-color: transparent; -fx-border-color: #3498db; -fx-border-width: 3px; -fx-border-radius: 10px; -fx-font-size: 18px;");
        try {
            HelloApplication app = new HelloApplication();
            app.escenario1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}