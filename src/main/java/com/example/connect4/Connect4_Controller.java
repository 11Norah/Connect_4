package com.example.connect4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Connect4_Controller {
    @FXML
    private Button start;
    @FXML
    private ComboBox AlgorithmsCombo;
    @FXML
    private ComboBox ColorsCombo;
    @FXML
    private TextField textfield_k;

    @FXML
    protected void display() {
        AlgorithmsCombo.getItems().setAll("Minimax without alpha-beta pruning", "Minimax with alpha-beta pruning");
    }

    @FXML
    protected void displayCo() {
        ColorsCombo.getItems().setAll("Red", "Yellow");
    }


    private Stage main_stage;
    @FXML
    protected void StartGame(ActionEvent event) throws IOException {
        main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //main_stage=new Stage();
        main_stage.setScene(new Scene
                (new FXMLLoader(Connect4_App.class.getResource("Connect4.fxml")).load()
                        , 800, 600));
        main_stage.show();
    }

}