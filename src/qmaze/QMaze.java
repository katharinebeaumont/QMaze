/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze;

import java.util.HashMap;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import qmaze.QLearning.Environment;
import qmaze.QLearning.QLearning;
import qmaze.QLearning.QTable;
import qmaze.QLearning.Room;

/**
 *
 * @author katharine
 * TODO: Fairly horrible class. Refactor.
 */
public class QMaze extends Application {
    
    private Environment env;
    private QLearning qLearning;
    private BorderPane border;
    final Slider gamma = new Slider(0, 1, 0.7); 
    final Slider epsilon = new Slider(0, 1, 0.1);    
    final Slider episodes = new Slider(0, 500, 250);  
    GridPane mapGrid = new GridPane();
    ImagePattern agent = new ImagePattern(new Image("/resources/agent.png"));
    ImagePattern agentAtGoal = new ImagePattern(new Image("/resources/agentAtGoal.png"));
    ImagePattern goal = new ImagePattern(new Image("/resources/goal.png")); 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        env = new Environment();
        env.setSmallerDefaultMaze(); 
        qLearning = new QLearning(env);
        border = new BorderPane();
        Pane buttonPane = addButtonPane();
        border.setTop(buttonPane);
        
        addMaze();
                        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 800);
        root.getChildren().add(border);
        
        primaryStage.setTitle("Q Learning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Pane addButtonPane() {
        
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle("-fx-background-color: #a5ea8a;");
        
        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        hboxTop.setStyle("-fx-background-color: #a5ea8a;");
        Button btnOpt = new Button();
        btnOpt.setText("Show optimal path");
        btnOpt.setOnAction((ActionEvent eventOpt) -> {
            System.out.println("Finding optimal path");
            HashMap<Integer, Pair> optimalPath = qLearning.findOptimalPath();
            animateMap(optimalPath);
        });
        btnOpt.setVisible(false);
            
        Button btn = new Button();
        btn.setText("Start training");
        btn.setOnAction((ActionEvent event) -> {
            System.out.println("Training");
            qLearning = new QLearning(env);
            addMaze();
            qLearning.startLearning(epsilon.getValue(), gamma.getValue(), (int)episodes.getValue());
            qLearning.printJourney();
            addQTable(qLearning.getQTable());
            btnOpt.setVisible(true);
        });
        
        hboxTop.getChildren().add(btn);
        hboxTop.getChildren().add(btnOpt);
        
        
        HBox hboxEp = new HBox();
        hboxEp.setPadding(new Insets(15, 12, 15, 12));
        hboxEp.setSpacing(10);
        hboxEp.setStyle("-fx-background-color: #a5ea8a;");
        final Label epsilonTitle = new Label(
        "Epsilon");
        
        epsilon.setShowTickLabels(true);
        epsilon.setShowTickMarks(true);
        epsilon.setMajorTickUnit(0.5);
        epsilon.setBlockIncrement(0.1);
        
        final Label epsilonLabel = new Label(
        Double.toString(epsilon.getValue()));
                
        epsilon.valueProperty().addListener((
            ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                epsilonLabel.setText(String.format("%.1f", new_val));
        });
        hboxEp.getChildren().addAll(epsilonTitle, epsilon, epsilonLabel);

        HBox hboxG = new HBox();
        hboxG.setPadding(new Insets(15, 12, 15, 12));
        hboxG.setSpacing(10);
        hboxG.setStyle("-fx-background-color: #a5ea8a;");
        final Label gammaTitle = new Label(
        "Gamma");
           
        gamma.setShowTickLabels(true);
        gamma.setShowTickMarks(true);
        gamma.setMajorTickUnit(0.5);
        gamma.setBlockIncrement(0.1);
        final Label gammaLabel = new Label(
        Double.toString(gamma.getValue()));
        
        gamma.valueProperty().addListener((
            ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                gammaLabel.setText(String.format("%.1f", new_val));
        });
        hboxG.getChildren().addAll(gammaTitle, gamma, gammaLabel);
        
        HBox hboxEpi = new HBox();
        hboxEpi.setPadding(new Insets(15, 12, 15, 12));
        hboxEpi.setSpacing(10);
        hboxEpi.setStyle("-fx-background-color: #a5ea8a;");
        final Label episodesTitle = new Label(
        "Episodes");
           
        episodes.setShowTickLabels(true);
        episodes.setShowTickMarks(true);
        episodes.setMajorTickUnit(500);
        episodes.setBlockIncrement(100);
        final Label episodesLabel = new Label(
        Double.toString(episodes.getValue()));
        
        episodes.valueProperty().addListener((
            ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                episodesLabel.setText(String.format("%d", new_val));
        });

        hboxEpi.getChildren().addAll(episodesTitle, episodes, episodesLabel);
        flow.getChildren().addAll(hboxTop,hboxEp,hboxG,hboxEpi);
        return flow;
    }
    
    public void addMaze() {
        mapGrid = new GridPane();
        mapGrid.setHgap(10);
        mapGrid.setVgap(10);
        mapGrid.setPadding(new Insets(10, 10, 10, 10));
        
        HashMap<Pair,Room> rooms = env.getRooms();
        for (HashMap.Entry<Pair,Room> entry : rooms.entrySet()) {
            Pair p = entry.getKey();
            Room room = entry.getValue();
            setMazeGridRoom(p, room);
        } 
        mapGrid.setAlignment(Pos.CENTER);
        border.setCenter(mapGrid);
    }

    private void setMazeGridRoom(Pair p, Room room) {
        int rowIndex = (int)p.getKey();
        int columnIndex = (int)p.getValue();
        Rectangle r = new Rectangle(50,50);
        Tooltip tp = new Tooltip("R:" + rowIndex + ", C:" + columnIndex);
        Tooltip.install(r, tp);
        if (room.getHasAgent() && room.getReward() > 0) {
            r.setFill(agentAtGoal);
        }else if (room.getHasAgent()) {
             r.setFill(agent);
        } else if (room.getReward() > 0) {
            r.setFill(goal);
        } else if (room.getOpen()) {
            r.setFill(Color.WHITE);
        } else {
            r.setFill(Color.GAINSBORO);
        }
        
        r.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent value) {
                boolean open = room.getOpen();
                room.setOpen(!open);
                setMazeGridRoom(p,room);
            }
        });
    
        mapGrid.add(r, columnIndex, rowIndex);
    }
    
    private void animateMap(HashMap<Integer, Pair> optimalPath) {
        
        //Put this back to normal
        Pair goalState = env.getGoalState();
        env.getRoom(goalState).setHasAgent(false);
        Pair startingState = env.getStartingState();
        env.getRoom(startingState).setHasAgent(true);
        
        System.out.println("Finding path");
        Set<Integer> stepsToGoal = optimalPath.keySet();
        int no_steps = stepsToGoal.size();
        System.out.println("Steps are: " + no_steps);
        long interval = 1000;
        //Whole animation should take around 30 seconds or less. If there are more than 6000
        // steps, which is highly unlikely, but anyway, don't bother because the
        // human eye wont see it (and your laptop has probably died by now). 
        if (no_steps > 6000) {
            throw new RuntimeException("Too many steps to display");
        }
        if (no_steps < 60 && no_steps > 30) {
            //So if we have more than 60 steps to the optimal path, 
            // then we want the interval to be smaller to accomodate this
            long millisAvailable = 30 * 1000;
            interval = millisAvailable/no_steps;
        }
        
        System.out.println("Interval is: " + interval);
        long timeMillis = 0;
        Pair previousPair = null;
        for (Integer key : stepsToGoal) {
            Timeline beat = new Timeline(
                new KeyFrame(Duration.millis(timeMillis),         event -> updateMaze(optimalPath, key))
            );
            beat.setAutoReverse(true);
            beat.setCycleCount(1);
            beat.play();
            timeMillis = timeMillis + interval;
        } 
    }

    private void updateMaze(HashMap<Integer, Pair> optimalPath, Integer key) {
        if (key > 0) {
            //Put previous room back to normal
            Pair previous = optimalPath.get(key-1);
            Room previousRoom = env.getRoom(previous);
            previousRoom.setHasAgent(false);
        }
        
        Pair p = optimalPath.get(key);
        
        Room currentRoom = env.getRoom(p);
        currentRoom.setHasAgent(true);
        System.out.println("Step is: " + p.toString());
        
        Node centreNode = border.getCenter();
        if (centreNode != null) {
            border.getChildren().remove(centreNode);
        }
        
        addMaze();
    }


    private void addQTable(QTable qTable) {
        
        BorderPane bp = new BorderPane();
        
        ScrollPane sp = new ScrollPane();
        
        GridPane grid = new GridPane();
        grid.setBorder(Border.EMPTY);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setStyle("-fx-background-color: #e4f9db");
        
        HashMap table = qTable.getTable();
        Set<Pair> roomCoordinates = table.keySet();
        for (Pair roomCoordinate: roomCoordinates) {
            Pane textPane = new Pane();
            
            int rowIndex = (int)roomCoordinate.getKey();
            int columnIndex = (int)roomCoordinate.getValue();
            StringBuilder sb = new StringBuilder();
            HashMap<Pair,Double> surroundingRooms = (HashMap)table.get(roomCoordinate);
            sb.append("Room ");
            sb.append(rowIndex);
            sb.append(",");
            sb.append(columnIndex);
            sb.append("\n");
            if (!env.getRoom(roomCoordinate).getOpen()) {
                sb.append("CLOSED");
            } else {
                for (HashMap.Entry<Pair,Double> entry : surroundingRooms.entrySet()) {
                    Pair nextRoom = entry.getKey();
                    sb.append(nextRoom.toString());
                    sb.append(": ");
                    sb.append(entry.getValue().toString());
                    sb.append("\n");
                }
            }
            Text t = new Text(sb.toString());
            
            textPane.getChildren().add(t);
            textPane.setStyle("-fx-background-color: #ffffff");
            textPane.setMaxHeight(75);
            grid.add(textPane, columnIndex, rowIndex);
        }
        grid.setAlignment(Pos.CENTER);
        sp.setContent(grid);
        Text title = new Text("Q Table");
        bp.setTop(title);
        bp.setCenter(sp);
        
        border.setRight(bp);        
    }
    
}
