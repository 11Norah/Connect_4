package com.example.connect4;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private TextField textfield_K;
    //for game page
    private static final int TileSize = 80;
    private static final int Columns = 7;
    private static final int Rows = 6;

    //used Variables
    private Stage main_stage;
    Boolean CompletedColumn=false;
    int[] ChipsInColumn=new int[Columns];
    public Boolean PlayerTurn=true;
    @FXML
    private Pane GamePane=new Pane();
    @FXML
    private Label status;

    private int levels;
    private Boolean PlayerColor=true;
    private Boolean Algorithm;

    public Connect4_Controller(int level,Boolean alg,Boolean Color){
        this.PlayerColor=Color;
        this.Algorithm=alg;
        this.levels=level;
    }
    @FXML
    protected void display() {
        AlgorithmsCombo.getItems().setAll("Minimax without alpha-beta pruning", "Minimax with alpha-beta pruning");
    }

    @FXML
    protected void displayCo() {
        ColorsCombo.getItems().setAll("Red", "Yellow");
    }
    private void alert_error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid ");
        alert.setHeaderText("Try again !");
        alert.setContentText(message);
        alert.show();
    }
    //------------------------------------------------------------------------------------------

    public void Restart(){
        GamePane.getChildren().clear();
        GamePane.getChildren().add(Board());
        //reset columns array to be empty
        for(int i=0;i<Columns;i++){
        ChipsInColumn[i]=0;
        }
    }
    public void AddChip(int Column_no, Boolean red){
        Circle new_chip=new Circle(TileSize/2);
        if(red){
        new_chip.setFill(Color.RED);}
        else{new_chip.setFill(Color.YELLOW);}
        new_chip.setCenterX(TileSize/2+3.5);
        new_chip.setCenterY(TileSize/2+3.5);
        new_chip.setTranslateX( Column_no*(TileSize+10)+TileSize/3);
       // new_chip.setTranslateY(((5-ChipsInColumn[Column_no]) * (TileSize + 10)) + TileSize / 3);
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5),new_chip );
        animation.setToY(((5-ChipsInColumn[Column_no]) * (TileSize + 10)) + TileSize / 3);
        animation.play();
        GamePane.getChildren().add(new_chip);

    }
    void PlayerTurn(int column_NO,Boolean col){
        if(PlayerTurn){
              System.out.println("color in turns"+col);
                AddChip(column_NO, col);

                ChipsInColumn[column_NO]++;

        }else{alert_error("It's Computer Turn ,wait!");}

    }
    private void ComputerTurn(){
        //after it plays
        //display that it is player's turn

    }
    Shape Board() {
        //to display board as shape
        Shape shape = new Rectangle((Columns + 1.5) * TileSize, (Rows + 1.5) * TileSize);

        for (int y = 0; y < Rows; y++) {
            for (int x = 0; x < Columns; x++) {
                Circle circle = new Circle(TileSize / 2);
                circle.setCenterX(TileSize / 2 +3);
                circle.setCenterY(TileSize / 2 +3);
                circle.setTranslateX(x * (TileSize + 10) + TileSize / 3);
                circle.setTranslateY(y * (TileSize + 10) + TileSize / 3);
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

        //Making invisible rectangles specifying columns and invisible circles in them
        //to determine mouse click when user plays

        for (int i=0;i<Columns;i++){
            Rectangle column=new Rectangle(TileSize+10,(Rows + 1.5) * TileSize);
            column.setTranslateX(i*(TileSize+10)+TileSize/4);
            column.setFill(Color.TRANSPARENT);

        column.setOnMouseEntered(e -> {
            column.setFill(Color.rgb(0,0,0,0.2));});
        column.setOnMouseExited(e -> {
            column.setFill(Color.TRANSPARENT);});

        int Column_no = i;
        Boolean col=this.PlayerColor;
        column.setOnMousePressed(e -> {
            System.out.println("ooooo"+ this.PlayerColor);
            System.out.println("00000 "+ this.levels);
            PlayerTurn(Column_no,col);
            //after player is done
            //PlayerTurn=false;
            status.setText("It's Computer Turn");
        });
        column.setOnMouseReleased(e -> {
            if(CompletedColumn) {
                //Coputer's turn
                //after it's done
                status.setText("Your turn");
                //PlayerTurn=true;
            }});

            GamePane.getChildren().add(column);
        }


        return shape;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Ini c "+this.PlayerColor);
        System.out.println("Ini l " +this.levels);
        System.out.println("Ini k"+this.Algorithm);
        GamePane.getChildren().addAll(Board());


    }
    void setVariables(){
        levels = Integer.parseInt(textfield_K.getText());

        if (ColorsCombo.getValue().equals( "Red")) {
            this.PlayerColor = true;
        } //if color =true ,then player is red
        else {
            this.PlayerColor = false;
        }


        if (AlgorithmsCombo.getValue().equals( "Minimax with alpha-beta pruning")) {
            Algorithm = true;
        } //algorithm is true if it is with pruning
        else {
            Algorithm = false;
        }
        System.out.println("Color is "+this.PlayerColor +this.Algorithm +this.levels);


    }
    @FXML
    protected void StartGame(ActionEvent event) throws IOException {

        //setting the values taken in initial page
        if(!textfield_K.getText().isEmpty() && ColorsCombo.getValue()!=null && AlgorithmsCombo!=null) {
            setVariables();
            main_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Connect4_App.class.getResource("Connect4.fxml"));
            System.out.println("Color in start  "+this.PlayerColor +this.Algorithm +this.levels);

            fxmlLoader.setControllerFactory(type -> {
                        if (type == Connect4_Controller.class) {
                            return new Connect4_Controller(this.levels,this.Algorithm,this.PlayerColor);
                        }
                        try {
                               return type.getConstructor().newInstance();
                } catch (Exception exc) {
                    // fatal...
                    throw new RuntimeException(exc);
                }
            });
            main_stage.setScene(new Scene(fxmlLoader.load(),1000,900));
            main_stage.setMaximized(true);
            main_stage.setResizable(false);
            main_stage.show();
            System.out.println("Color is "+this.PlayerColor);

        }
        else{ //display error
            System.out.println("Complete empty fields");
            alert_error("Complete missing fields first,please!");}




    }
}