package com.example.connect4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Connect4_Controller implements Initializable{
    //for initial page
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
    //------------------------------------------------------------------------------------------
    private Stage main_stage;
    private int levels;
    private Boolean color;
    private Boolean Algorithm;

    private static final int TileSize = 80;
    private static final int Columns = 7;
    private static final int Rows = 6;

    @FXML
    private Pane GamePane=new Pane();




    @FXML
    protected void StartGame(ActionEvent event) throws IOException {
        main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //main_stage=new Stage();
        main_stage.setScene(new Scene
                (new FXMLLoader(Connect4_App.class.getResource("Connect4.fxml")).load(),1000,900));
        main_stage.setMaximized(true);
        main_stage.setResizable(false);
        main_stage.show();

        //setting the values taken in initial page
        if(textfield_k!=null && ColorsCombo!=null && AlgorithmsCombo!=null) {
            levels = Integer.parseInt(textfield_k.getText());

            if (ColorsCombo.getValue() == "Red") {
                color = true;
            } //if color =true ,then player is red
            else {
                color = false;
            }
            if (AlgorithmsCombo.getValue() == "Minimax with alpha-beta pruning") {
                Algorithm = true;
            } //algorithm is true if it is with pruning
            else {
                Algorithm = false;
            }
        }
        else{ //display error
            System.out.println("Complete empty fields");}


    }

    private Shape Board() {
        Shape shape = new Rectangle((Columns + 1) * TileSize, (Rows + 1) * TileSize);

        for (int y = 0; y < Rows; y++) {
            for (int x = 0; x < Columns; x++) {
                Circle circle = new Circle(TileSize / 2);
                circle.setCenterX(TileSize / 2 +3);
                circle.setCenterY(TileSize / 2 +3);
                circle.setTranslateX(x * (TileSize + 5) + TileSize / 3);
                circle.setTranslateY(y * (TileSize + 5) + TileSize / 3);
                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(45.0);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GamePane.getChildren().add(Board());
    }
}