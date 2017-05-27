package qmaze.View.MazeComponents;

import qmaze.View.TrainingConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import qmaze.Environment.Coordinates;
import qmaze.View.Components.Component;
import qmaze.View.ViewController;

/**
 *
 * @author katharine
 */
public class QMazeGrid extends Component {
    
    private static final long ANIMATION_INTERVAL = 500;
    
    private ArrayList<QMazeRoom> rooms;
    BorderPane mainBackground = new BorderPane();
    ScrollPane scrollPane = new ScrollPane();
    BorderPane subBackground = new BorderPane();
    
    private int rows;
    private int columns;
    private Coordinates startingState;
    private Coordinates goalState;
    private Coordinates agentLocation;
    private final String SET_START = "Set starting room";
    private final String SET_GOAL = "Set goal room";
    
    public QMazeGrid(ViewController controller) {
        super(controller);
        initialiseMazeRooms();
    }
    
    private void initialiseMazeRooms() {
        TrainingConfig mazeConfig = controller.getQMazeConfig();
        this.rows = mazeConfig.getRows();
        this.columns = mazeConfig.getColumns();
        if (startingState == null) {
            this.startingState = new Coordinates(0,0);
        }
        if (goalState == null) {
             this.goalState = new Coordinates(columns-1,rows-1);
        }
        
        this.rooms = new ArrayList();
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                QMazeRoom room = new QMazeRoom(new Coordinates(i, j));
                rooms.add(room);
            }
        }
        
        scrollPane.setContent(subBackground);
        mainBackground.setCenter(scrollPane);
    }
    
    private void redrawMaze() {
        
        Node centreNode = subBackground.getCenter();
        if (centreNode != null) {
            subBackground.getChildren().remove(centreNode);
        }
        build();
    }

    @Override
    public void reset() {
        setAgentLocation(null);
        if (controller.STATE.equals(RESET_STATE) || controller.STATE.equals(ADJUST_PARAM_STATE)) {
            //Reset maze, according to controller's instructions
            initialiseMazeRooms();
        }  
        if (controller.STATE.equals(TRAINED_STATE)) {
            //Show heatmap
            HashMap heatMap = controller.getHeatMap();
            showVisitCount(heatMap);
        }
        if (controller.STATE.equals(ADJUST_MAZE_STATE)) {
            //Clear heatmap
            showVisitCount(new HashMap());
        }
        redrawMaze();
    }

    @Override
    public Pane build() {
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        
        for (QMazeRoom room: rooms) {
            
            if (room.getCoordinates().equals(goalState)) {
                room.setReward(100);
            } else {
                room.setReward(0);
            }
            int columnIndex = room.getCoordinates().getXCoordinate();
            int rowIndex = room.getCoordinates().getYCoordinate();
            Rectangle r = new Rectangle(50,50);
            
            boolean open = room.getOpen();
            Coordinates roomLocation = room.getCoordinates();
            boolean hasAgent = agentLocation != null && roomLocation.equals(agentLocation);
            double percentageVisits = room.getPercentageVisits();
            double totalVisits = room.getVisitCount();
            double fillFactor = (percentageVisits + totalVisits) / 2;
            StackPane stack = new StackPane();
            stack.setShape(r);
            Paint p;
            if (open) {
                p = getHeatMapColor(fillFactor);
            } else {
                p = Color.DARKGRAY;
            }
            
            BackgroundFill bf = new BackgroundFill(p, null, null);
            stack.setBackground(new Background(bf));
                
            Rectangle r2 = new Rectangle(50,50);
            
            if (roomLocation.equals(goalState)) {
                if (hasAgent) {
                    r2.setFill(assets.getAgentAtGoalImage());
                } else {
                    r2.setFill(assets.getGoalImage());
                }
                //r2.setOpacity(0.4);
            } else if (roomLocation.equals(startingState) && !hasAgent) {
                r2.setOpacity(0);
                stack.getChildren().add(new Label("X"));
            } 
            else if (hasAgent) {
                r2.setFill(assets.getAgentImage());
                //r2.setOpacity(0.4);
            } else {
                r2.setOpacity(0);
            }
            stack.getChildren().add(r2);
            
            String percentageVisitDesc = (int)(percentageVisits*100) + "%";
            Tooltip tp = new Tooltip("R:" + rowIndex + ", C:" + columnIndex + ", V:" + percentageVisitDesc);
            Tooltip.install(stack, tp);
            
            stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent value) {
                    
                    if (value.isControlDown()) {
                        openOrCloseRoom(room);
                    } else {
                        configureRoom(room);
                    }
                }

                
            });
    
            gridPane.add(stack, columnIndex, rowIndex);
        }
        
        subBackground.setCenter(gridPane);
        return mainBackground;
    }

    private void openOrCloseRoom(QMazeRoom room) {
        if (room.getCoordinates().equals(startingState) || room.getCoordinates().equals(goalState)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Oh dear");
            alert.setHeaderText("Trying to close this room?");
            alert.setContentText("You can't close it!");
            alert.showAndWait();
        } else {
            boolean open = room.getOpen();
            room.open(!open);
            redrawMaze();
            controller.roomReset();
        }
    }
    
    private void configureRoom(QMazeRoom room) {
        
        ObservableList<String> options = 
        FXCollections.observableArrayList(
            SET_START,
            SET_GOAL
        );
        ChoiceDialog cd = new ChoiceDialog(SET_START, options);
        cd.setTitle("Configure Rooms");
        cd.setHeaderText("Change the starting or goal room");
        Optional<String> result = cd.showAndWait();
        result.ifPresent(selected -> changeRoom(selected, room));
    }
    
    private void changeRoom(String selected, QMazeRoom room) {
        Coordinates roomLocation = room.getCoordinates();
        boolean isStartingState = roomLocation.equals(startingState);
        boolean isGoalState = roomLocation.equals(goalState);
        if (selected.equals(SET_START) && !isGoalState) {
            System.out.println("Changing starting state to " + roomLocation.toString());
            setStartingState(roomLocation);
            controller.roomReset();
        }
        if (selected.equals(SET_GOAL) && !isStartingState) {
            System.out.println("Changing goal state to " + roomLocation.toString());
            setGoalState(roomLocation);
            controller.roomReset();
        }
    }
    
    /**
     * Animation/heatmap stuff
     */
    public void showVisitCount(HashMap<Coordinates, Integer> heatMap) {
    
        if (heatMap == null) {
            return;
        }
        
        //Get max visit count
        double maxVisit = getTotalVisitCount(heatMap);
        double highestVisit = getHighestVisitCount(heatMap);
        
        for (QMazeRoom room: rooms) {
            Coordinates roomLocation = room.getCoordinates();
            if (heatMap.containsKey(roomLocation)) {
                double roomVisits = heatMap.get(roomLocation);
                room.setPercentageVisits(roomVisits/maxVisit);
                room.setVisitCount(roomVisits/highestVisit);
            } else {
                room.setPercentageVisits(0);
                room.setVisitCount(0);
            }
        }
        redrawMaze();
    }
    
    private double getTotalVisitCount(HashMap<Coordinates, Integer> heatMap) {
        double totalVisits = 0;
        Set<Coordinates> keys = heatMap.keySet();
        for (Coordinates key: keys) {
            int value = heatMap.get(key);
            totalVisits = totalVisits + (double)value;
        }
        return totalVisits;
    }
    
    private double getHighestVisitCount(HashMap<Coordinates, Integer> heatMap) {
        int highestVisit = 0;
        Set<Coordinates> keys = heatMap.keySet();
        for (Coordinates key: keys) {
            int value = heatMap.get(key);
            if (value > highestVisit) {
                highestVisit = value;
            }
        }
        return (double)highestVisit;
    }
    
    private Paint getHeatMapColor(double visitCount) {
        //Want to exaggerate more heavily visited rooms
        Color fillColor = Color.color(1, 1, 1);
        String heatMapColour = controller.getHeatMapColour();
        switch(heatMapColour) {
            case("Green"):
                fillColor = Color.color((1-visitCount), 1, (1-visitCount));
                break;
            case("Yellow"):
                fillColor = Color.color(1, 1, (1-visitCount));
                break;
            case("Pink"):
                fillColor = Color.color(1, (1-visitCount), 1);
                break;
            case("Cyan"):
                fillColor = Color.color((1-visitCount), 1, 1);
                break;
            case("Blue"):
                fillColor = Color.color((1-visitCount), (1-visitCount), 1);
                break;
            case("Red"):
                fillColor = Color.color(1, (1-visitCount), (1-visitCount)); 
                break;
            default:
                break;
        }
        return fillColor;
    }
    
    public void animateMap(ArrayList<Coordinates> optimalPath) {
        
        animateAgent(startingState);

        System.out.println("Finding path");
        int stepsToGoal = optimalPath.size();
        long interval = getInterval(stepsToGoal);
        System.out.println("Interval is: " + interval);
        long timeMillis = 0;
        for (Coordinates agentLocation : optimalPath) {
            
            Timeline beat = new Timeline(
                new KeyFrame(Duration.millis(timeMillis),         event -> animateAgent(agentLocation))
            );
            beat.setAutoReverse(true);
            beat.setCycleCount(1);
            beat.play();
            timeMillis = timeMillis + interval;
        } 
    }

    private void animateAgent(Coordinates roomWithAgent) {
        setAgentLocation(roomWithAgent);
        redrawMaze();
    }
    
    private long getInterval(int stepsToGoal) {
        
        System.out.println("Steps are: " + stepsToGoal);
        long interval = ANIMATION_INTERVAL;
        //Whole animation should take around 30 seconds or less. If there are more than 6000
        // steps, which is highly unlikely, but anyway, don't bother because the
        // human eye wont see it (and your laptop has probably died by now). 
        if (stepsToGoal > 6000) {
            throw new RuntimeException("Too many steps to display");
        }
        if (stepsToGoal > 30) {
            //So if we have more than 60 steps to the optimal path, 
            // then we want the interval to be smaller to accomodate this
            long millisAvailable = 30 * 1000;
            interval = millisAvailable/(long)stepsToGoal;
        }
        return interval;
    }
    
    
    
    /**
     * Getters/setters
     */
    public ArrayList<QMazeRoom> getRooms() {
        return rooms;
    }
    
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    
    public Coordinates getStartingState() {
        return startingState;
    }

    public void setStartingState(Coordinates startingState) {
        this.startingState = startingState;
    }
        
    public Coordinates getGoalState() {
        return goalState;
    }
    
    public void setGoalState(Coordinates goalState) {
        this.goalState = goalState;
    }
    
    public Coordinates getAgentLocation() {
        return agentLocation;
    }
    
    private void setAgentLocation(Coordinates agentLocation) {
        this.agentLocation = agentLocation;
    }
    
}
