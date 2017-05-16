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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
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
import qmaze.QLearning.QLearningConfig;
import qmaze.QLearning.QTable;
import qmaze.QLearning.Room;

/**
 *
 * @author katharine
 * TODO: Fairly horrible class. Refactor.
 * TODO: bug fix: episodes
 * TODO: one big scroll pane for bottom bit
 * TODO: color in QTable
 */
public class QMaze extends Application {
    
    private Environment env;
    private QLearning qLearning;
    
    private final int SCREEN_WIDTH = 900;
    private final int SCREEN_HEIGHT = 600;
    private final int ANIMATION_INTERVAL = 500;
    
    private BorderPane border;
    private final double initialGamma = 0.7;
    private final double initialEpsilon = 0.1;
    private final double initialAlpha = 0.1;
    private final int initialEpisodes = 50;
    final Slider gamma = new Slider(0, 1, initialGamma); 
    final Slider epsilon = new Slider(0, 1, initialEpsilon);    
    final Slider alpha = new Slider(0, 1, initialAlpha);    
    private final int initialRows = 4;
    private final int initialColumns = 4;
    final IntegerSpinnerValueFactory mazeSpinnerRows = new IntegerSpinnerValueFactory(2,16,initialRows);
    final IntegerSpinnerValueFactory mazeSpinnerColumns = new IntegerSpinnerValueFactory(2,16,initialColumns);
    final IntegerSpinnerValueFactory mazeSpinnerEpisodes = new IntegerSpinnerValueFactory(1,100,initialEpisodes);
    GridPane mapGrid = new GridPane();
    ImagePattern agent = new ImagePattern(new Image("/resources/agent.png"));
    ImagePattern agentAtGoal = new ImagePattern(new Image("/resources/agentAtGoal.png"));
    ImagePattern goal = new ImagePattern(new Image("/resources/goal.png")); 
    private final Button btnOptimalPath = new Button();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        border = new BorderPane();
        addButtonPane();
        
        resetMaze();
        addMaze();
                        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(border);
        
        primaryStage.setTitle("Q Learning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void reset() {
        addButtonPane();
        gamma.setValue(initialGamma);
        epsilon.setValue(initialEpsilon);
        alpha.setValue(initialAlpha);
        mazeSpinnerEpisodes.setValue(initialEpisodes);
        mazeSpinnerRows.setValue(initialRows);
        mazeSpinnerColumns.setValue(initialColumns);
        resetMaze();
        addMaze();
    }
    
    private void resetMaze() {
        env = new Environment(mazeSpinnerRows.getValue(), mazeSpinnerColumns.getValue());
        resetQTable();
    }

    private void resetQTable() {
        btnOptimalPath.setDisable(true);
        removeQTable();
    }
    
    private void addButtonPane() {
        
        FlowPane flow = new FlowPane(Orientation.VERTICAL);
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setStyle("-fx-background-color: #a5ea8a;");
        flow.setMaxHeight(200);
        
        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        
        btnOptimalPath.setText("Show optimal path");
        btnOptimalPath.setOnAction((ActionEvent eventOpt) -> {
            System.out.println("Finding optimal path");
            HashMap<Integer, Pair> optimalPath = qLearning.findOptimalPath();
            animateMap(optimalPath);
        });
        btnOptimalPath.setDisable(true);
            
        Button btn = new Button();
        btn.setText("Start training");
        btn.setOnAction((ActionEvent event) -> {
            System.out.println("Training");
            QLearningConfig config = new QLearningConfig(mazeSpinnerEpisodes.getValue(), gamma.getValue(), epsilon.getValue(), alpha.getValue());
            qLearning = new QLearning(env,config);
            qLearning.startLearning();
            addQTable(qLearning.getQTable());
            btnOptimalPath.setDisable(false);
        });
        
        Button btnReset = new Button();
        btnReset.setText("Reset");
        btnReset.setOnAction((ActionEvent event) -> {
            System.out.println("Resetting");
            reset();
        });
        
        hboxTop.getChildren().addAll(btn, btnOptimalPath, btnReset);
        
        HBox hboxEp = buildSlider(epsilon, "Probability Explore", 1);
        HBox hboxG = buildSlider(gamma, "Reward Discount", 1);
        HBox hboxA = buildSlider(alpha, "Learning Rate", 1);
        HBox hboxSliders = new HBox();
        hboxSliders.getChildren().addAll(hboxEp,hboxG,hboxA);
        
        HBox hboxsp = buildSpinners();
        
        flow.getChildren().addAll(hboxTop,hboxSliders,hboxsp);
        border.setTop(flow);
    }
    
    private HBox buildSlider(Slider slider, String labelTitle, double max) {
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        
        final Label title = new Label(
        labelTitle);
        
        double majorTickUnit = max/2;
        double minorTickUnit = max/10;
        
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(majorTickUnit);
        slider.setBlockIncrement(minorTickUnit);
        
        final Label label = new Label(
        Double.toString(slider.getValue()));
                
        slider.valueProperty().addListener((
            ObservableValue<? extends Number> ov, 
            Number old_val, Number new_val) -> {
                label.setText(String.format("%.1f", new_val));
                resetQTable();
        });
        hbox.getChildren().addAll(title, slider, label);
        return hbox;
    }
    
    private HBox buildSpinners() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        Spinner rowSpinner = new Spinner(mazeSpinnerRows);
        Spinner columnSpinner = new Spinner(mazeSpinnerColumns);
        Spinner episodeSpinner = new Spinner(mazeSpinnerEpisodes);
        episodeSpinner.setEditable(true);
        final Label labelRows = new Label("Rows");
        final Label labelCols = new Label("Columns");
        final Label labelEpisodes = new Label("Episodes");
        rowSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               resetMaze(); 
               addMaze();
        });
        columnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               resetMaze(); 
               addMaze();
        });
        episodeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
               mazeSpinnerEpisodes.setValue((int)newValue);
        });
        
        hbox.getChildren().addAll(labelRows, rowSpinner, labelCols, columnSpinner, labelEpisodes, episodeSpinner);
        return hbox;
    }
    
    private void addMaze() {
        BorderPane background = new BorderPane();
        ScrollPane sp = new ScrollPane();
        
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
        
        sp.setContent(mapGrid);
        background.setCenter(sp);
        
        border.setCenter(background);
        
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
                btnOptimalPath.setDisable(true);
                removeQTable();
            }
        });
    
        mapGrid.add(r, columnIndex, rowIndex);
    }
    
    private void animateMap(HashMap<Integer, Pair> optimalPath) {
        
        //Put this back to normal
        env.resetAgent();
        addMaze();
        
        System.out.println("Finding path");
        Set<Integer> stepsToGoal = optimalPath.keySet();
        long interval = getInterval(stepsToGoal);
        System.out.println("Interval is: " + interval);
        long timeMillis = 0;
        for (Integer key : stepsToGoal) {
            Pair previousRoom = optimalPath.get(key-1);
            Timeline beat = new Timeline(
                new KeyFrame(Duration.millis(timeMillis),         event -> updateMaze(optimalPath.get(key), previousRoom))
            );
            beat.setAutoReverse(true);
            beat.setCycleCount(1);
            beat.play();
            timeMillis = timeMillis + interval;
        } 
    }

    private void updateMaze(Pair currentCoordinates, Pair previousRoomCoordinates) {
        if (previousRoomCoordinates != null) {
            //Put previous room back to normal
            Room previousRoom = env.getRoom(previousRoomCoordinates);
            previousRoom.setHasAgent(false);
        }
        
        Room currentRoom = env.getRoom(currentCoordinates);
        currentRoom.setHasAgent(true);
        
        Node centreNode = border.getCenter();
        if (centreNode != null) {
            border.getChildren().remove(centreNode);
        }
        
        addMaze();
    }

    private void removeQTable() {
        Node qTable = border.getRight();
        border.getChildren().remove(qTable);
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
            sb.append("Room ");
            sb.append(rowIndex);
            sb.append(",");
            sb.append(columnIndex);
            sb.append("\n");
            if (!env.getRoom(roomCoordinate).getOpen()) {
                sb.append("CLOSED");
                textPane.setStyle("-fx-background-color: #DCDCDC");
            } else if (env.getGoalState().equals(roomCoordinate)) {
                sb.append("GOAL");
                textPane.setStyle("-fx-background-color: #FFD700");
            } else {
                HashMap<Pair,Double> surroundingRooms = (HashMap)table.get(roomCoordinate);
                for (HashMap.Entry<Pair,Double> entry : surroundingRooms.entrySet()) {
                    Pair nextRoom = entry.getKey();
                    sb.append(nextRoom.toString());
                    sb.append(": ");
                    String qValue = String.format("%.2f", entry.getValue());
                    sb.append(qValue);
                    sb.append("\n");
                    textPane.setStyle("-fx-background-color: #ffffff");
                }
            }
            Text t = new Text(sb.toString());
            
            textPane.getChildren().add(t);
            textPane.setMaxHeight(75);
            textPane.setMaxWidth(75);
            grid.add(textPane, columnIndex, rowIndex);
        }
        sp.setContent(grid);
        Text title = new Text("Q Table");
        bp.setTop(title);
        bp.setCenter(sp);
        bp.setMaxWidth(SCREEN_WIDTH/2);
        border.setRight(bp);        
    }


    private long getInterval(Set<Integer> stepsToGoal) {
        int no_steps = stepsToGoal.size();
        System.out.println("Steps are: " + no_steps);
        long interval = ANIMATION_INTERVAL;
        //Whole animation should take around 30 seconds or less. If there are more than 6000
        // steps, which is highly unlikely, but anyway, don't bother because the
        // human eye wont see it (and your laptop has probably died by now). 
        if (no_steps > 6000) {
            throw new RuntimeException("Too many steps to display");
        }
        if (no_steps > 30) {
            //So if we have more than 60 steps to the optimal path, 
            // then we want the interval to be smaller to accomodate this
            long millisAvailable = 30 * 1000;
            interval = millisAvailable/no_steps;
        }
        return interval;
    }
    
}
