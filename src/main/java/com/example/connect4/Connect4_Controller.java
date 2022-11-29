package com.example.connect4;
import com.example.connect4.Solver.ISolver;
import com.example.connect4.Solver.MinimaxWithPruningSolver;
import com.example.connect4.Solver.MinimaxWithoutPruningSolver;
import com.example.connect4.heuristics.Heuristic;
import com.example.connect4.heuristics.IHeuristic;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.pow;

public class Connect4_Controller implements Initializable{
     long initial_state=0;
     State previous;
    BitsArray bits=new BitsArray(initial_state);
    ISolver instance_solve;

    //for initial page
    @FXML
    private Button start;
    @FXML
    private ComboBox AlgorithmsCombo;
    @FXML
    private ComboBox ColorsCombo;
    @FXML
    private TextField textfield_K;
    @FXML
    private Button view;
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
    @FXML
    private Label Player_score;
    @FXML
    private Label Computer_score;
    @FXML
    private Label Time;
    @FXML
    private Label Nodes;

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
        this.PlayerTurn=true;
        this.count=0;
        status.setText("Player's  turn");
        Player_score.setText("");
        Computer_score.setText("");
        //reset columns array to be empty
        for(int i=0;i<Columns;i++){
        ChipsInColumn[i]=0;
        }
        Nodes.setText("");
        Time.setText("");


    }
    public void AddChip(int Column_no, Boolean red,char turn){
        TranslateTransition animation;
        Circle new_chip=new Circle(TileSize/2);
        if(red){
        new_chip.setFill(Color.RED);}
        else{new_chip.setFill(Color.YELLOW);}
        new_chip.setCenterX(TileSize/2+3.5);
        new_chip.setCenterY(TileSize/2+3.5);
        new_chip.setTranslateX( Column_no*(TileSize+10)+TileSize/3);
       // new_chip.setTranslateY(((5-ChipsInColumn[Column_no]) * (TileSize + 10)) + TileSize / 3);
        if (turn=='h') {
            animation = new TranslateTransition(Duration.seconds(0.05), new_chip);
        }else{
            animation = new TranslateTransition(Duration.seconds(0.4), new_chip);
            animation.pause();
        }
        animation.setToY(((5-ChipsInColumn[Column_no]) * (TileSize + 10)) + TileSize / 3);
        animation.play();
        GamePane.getChildren().add(new_chip);

    }
    void PlayerTurn(int column_NO,Boolean col){
        if(PlayerTurn){

                System.out.println("color in turns"+col);
                //display chips on board
                AddChip(column_NO, col,'h');
                ChipsInColumn[column_NO]++;
                //setting this state on back


        }else{alert_error("It's Computer Turn ,wait!");}

    }
    int count=0;
    private void ComputerTurn(int col){
        count++;
         long startTime, endTime;
      if(count==1){
        this.previous=new State(bits,true);}


        System.out.println("previous State :"+this.previous.getBoard());

        if(this.Algorithm){ //with_pruning
             instance_solve=new MinimaxWithPruningSolver();
            IHeuristic heu=new Heuristic();

            startTime = System.nanoTime() / (long) pow(10, 3);
            instance_solve.solve(heu,this.levels,previous,col);
            endTime = System.nanoTime() / (long) pow(10, 3);

            int comp_col=instance_solve.getChangedColumn();
           // System.out.println(instance_solve.getTree().getRoot().getChildren());
            //System.out.println("column comp :"+comp_col);
            AddChip(comp_col,!this.PlayerColor,'c');
            ChipsInColumn[instance_solve.getChangedColumn()]++;
            this.previous=instance_solve.getChosenState();

        }else{
            //without
             instance_solve=new MinimaxWithoutPruningSolver();
            IHeuristic heu=new Heuristic();
            startTime = System.nanoTime() / (long) pow(10, 3);
            instance_solve.solve(heu,this.levels,previous,col);
           endTime = System.nanoTime() / (long) pow(10, 3);
            int comp_col=instance_solve.getChangedColumn();

            //System.out.println("column comp :"+comp_col);
            AddChip(comp_col,!this.PlayerColor,'c');
            ChipsInColumn[instance_solve.getChangedColumn()]++;
            this.previous=instance_solve.getChosenState();
        }
        System.out.println("                                "+instance_solve.getTree().getRoot().getHeuristics()+"                                ");
        System.out.println();
        System.out.println();
        System.out.println();
        for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {
            System.out.print("     " + instance_solve.getTree().getRoot().getChildren().get(i).getHeuristics() + "    ");

        }
        System.out.println();
       for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {
           System.out.print("     " + "/" + "    ");
           System.out.print("      \\     ");
           System.out.print("      \\     ");
           System.out.print("      \\     ");
           System.out.print("      \\     ");
           System.out.print("      \\     ");
       }
       System.out.println();
           for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {

               System.out.print("    " + "/" + "   ");
           }
        System.out.println();

        for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {

            System.out.print("   " + "/" + "  ");
        }
        System.out.println();

        for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {

            System.out.print( " /"+"    ");
        }
        System.out.println();

        for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {
            for (int j = 0; j < instance_solve.getTree().getRoot().getChildren().get(i).getChildren().size(); j++) {
                System.out.print(instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getHeuristics() + "    ");
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();
        for(int i=0;i<instance_solve.getTree().getRoot().getChildren().size();i++) {
            for (int j = 0; j < instance_solve.getTree().getRoot().getChildren().get(i).getChildren().size(); j++) {
                for (int k=0;k<instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getChildren().size();k++){
                    System.out.print(instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getHeuristics()+"    ");
                }
            }
        }
        //after it plays
        //display that it is player's turn
        Computer_score.setText(String.valueOf(this.previous.getComputerScore()));
        Player_score.setText(String.valueOf(this.previous.getHumanScore()));
        System.out.println("Time Taken : "+ (endTime-startTime) +"micro seconds");
        System.out.println("Nodes Expanded: "+ instance_solve.getTree().getExpandedNodes());
        Time.setText("Time Taken : " +(endTime-startTime) +" MicroSeconds");
        Nodes.setText("Nodes Expanded : "+ instance_solve.getTree().getExpandedNodes());
        status.setText("Your turn");

        PlayerTurn=true;
        view.setDisable(false);
        //the end of the game
        if(this.previous.isEndState()){
            if(this.previous.getComputerScore()<this.previous.getHumanScore())
            {Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations ");
            alert.setHeaderText("You are the winner !"); alert.show(); Restart();}
            else{Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unfortunately ");
                alert.setHeaderText("You lose the game !");
                alert.setContentText("Hard luck ! Reset the game to play again");
                alert.show();
            }

        }


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
            PlayerTurn=false;
            status.setText("It's Computer Turn");

        });

        column.setOnMouseReleased(e -> {
          status.setText("It's  computer  turn");
                    //Coputer's turn
                    //after it's done
                    System.out.println("NOW COMPUTER" + Column_no);
                    PlayerTurn = true;
                    //PlayerTurn=true;
            ComputerTurn(Column_no);});

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
    @FXML
    protected  void viewTree() {
        AnchorPane secondaryLayout = new AnchorPane();
        String computercolor;

        secondaryLayout.setPrefSize(50000,800);
        ScrollPane sp=new ScrollPane();
        Group root=new Group();
        root.getChildren().addAll(sp);
        sp.setPrefSize(5000, 800);
        sp.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        Scene secondScene = new Scene(root, 1000, 600);
        Circle c1=new Circle();
        c1.setCenterX(9400.0f);
        c1.setCenterY(90.0f);
        c1.setRadius(20.0f);
        if(PlayerColor==true){
            c1.setFill(Color.YELLOW);
        }
        else{
            c1.setFill(Color.RED);
        }
        Text t1 = new Text(c1.getCenterX()-12, c1.getCenterY(),Integer.toString(instance_solve.getTree().getRoot().getHeuristics()));
        secondaryLayout.getChildren().add(c1);
        secondaryLayout.getChildren().add(t1);
        int size1=instance_solve.getTree().getRoot().getChildren().size();
        int size2,size3;
        int flag1=0,flag2=0,flag3=0;
        for (int i=0;i<size1;i++){
            Circle c2=new Circle();
            Line l1=new Line(9400,110,1300+2700*i,280);

            if(flag1==1){
                l1.setStroke(Color.BLACK);
            }
            c2.setCenterX(1300.0f+2700*i);
            c2.setCenterY(300.0f);
            c2.setRadius(20.0f);
            if(PlayerColor==true){
                c2.setFill(Color.RED);
            }
            else{
                c2.setFill(Color.YELLOW);
            }

            Text t2 = new Text(c2.getCenterX()-12, c2.getCenterY(),Integer.toString(instance_solve.getTree().getRoot().getChildren().get(i).getHeuristics()));
            secondaryLayout.getChildren().add(c2);
            secondaryLayout.getChildren().add(t2);
            secondaryLayout.getChildren().add(l1);
            size2=instance_solve.getTree().getRoot().getChildren().get(i).getChildren().size();
            System.out.println("b5"+size2);
            for(int j=0;j<size2;j++){
                Circle c3=new Circle();
                Line l2=new Line(1300+2700*i,320,240+355*j+365*(size1+.2)*i,580);
                if(flag2==1){
                    l2.setStroke(Color.BLACK);
                }
                c3.setCenterX(240.0f+355*j+365*(size1+.2)*i);
                c3.setCenterY(600.0f);
                c3.setRadius(20.0f);
                if(PlayerColor==true){
                    c3.setFill(Color.YELLOW);
                }
                else{
                    c3.setFill(Color.RED);
                }
                secondaryLayout.getChildren().add(c3);
                secondaryLayout.getChildren().add(l2);

                Text t3 = new Text(c3.getCenterX()-12, c3.getCenterY(),Integer.toString(instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getHeuristics()));
                secondaryLayout.getChildren().add(t3);
                size3=instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getChildren().size();
                for(int k=0;k<size3;k++){
                    Circle c4=new Circle();
                    Line l3=new Line(240+355*j+365*(size1+.2)*i,620,40+50*k+355*j+365*(size1+.2)*i,880);
                    if(flag3==1){
                        l3.setStroke(Color.BLACK);
                    }
                    else if(instance_solve.getPathToGoal()[0].getHeuristics()==instance_solve.getTree().getRoot().getChildren().get(i).getHeuristics()&&
                            instance_solve.getPathToGoal()[1].getHeuristics()==instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getHeuristics()&&
                            instance_solve.getPathToGoal()[2].getHeuristics()==instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getHeuristics()){

                        l3.setStroke(Color.GREEN);
                        l3.setStrokeWidth(5);
                        flag3=1;
                        l2.setStroke(Color.GREEN);
                        l2.setStrokeWidth(5);
                        flag2=1;
                        l1.setStroke(Color.GREEN);
                        l1.setStrokeWidth(5);
                        flag1=1;

                    }
                    c4.setCenterX(40+50*k+355*j+365*(size1+.2)*i);
                    c4.setCenterY(900.0f);
                    c4.setRadius(20.0f);
                    if(PlayerColor==true){
                        c4.setFill(Color.RED);
                    }
                    else{
                        c4.setFill(Color.YELLOW);
                    }
                    Text t4 = new Text(c4.getCenterX()-12, c4.getCenterY(),Integer.toString(instance_solve.getTree().getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getHeuristics()));
                    secondaryLayout.getChildren().add(c4);
                    secondaryLayout.getChildren().add(l3);
                    secondaryLayout.getChildren().add(t4);
                }
            }
        }
        sp.setContent(secondaryLayout);

        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");

        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.show();
    }
}