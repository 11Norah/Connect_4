package com.example.connect4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Connect4_App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Connect4_App.class.getResource("Initial_page.fxml"));
        fxmlLoader.setControllerFactory(type -> {
            if (type == Connect4_Controller.class) {
                return new Connect4_Controller(0,true,true);
            }
            try {
                return type.getConstructor().newInstance();
            } catch (Exception exc) {
                // fatal...
                throw new RuntimeException(exc);
            }
        });
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Connect 4");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}