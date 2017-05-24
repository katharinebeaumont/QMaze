package qmaze.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class QMazeGrid {
    private ArrayList<QMazeRoom> rooms;
    private GridPane gridPane;
    private BorderPane border;
    private final int rows;
    private final int columns;
    private final Coordinates startingState;
    private final Coordinates goalState;
    private Coordinates agentLocation;
    private String heatMapColor = QMazeFixedParams.HEATMAP_COLOR;
    
    ImagePattern agent = new ImagePattern(new Image("/resources/agent.png"));
    ImagePattern agentAtGoal = new ImagePattern(new Image("/resources/agentAtGoal.png"));
    ImagePattern goal = new ImagePattern(new Image("/resources/goal.png")); 
    
    public QMazeGrid(int rows, int columns, BorderPane border) {
        
        this.rows = rows;
        this.columns = columns;
        this.border = border;
        startingState = new Coordinates(0,0);
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        
        //TODO: should be configurable
        goalState = new Coordinates(columns-1,rows-1);
        
        rooms = new ArrayList();
        for (int i=0; i<columns; i++) {
            for (int j=0; j<rows; j++) {
                QMazeRoom room = new QMazeRoom(new Coordinates(i, j));
                if (room.getCoordinates().equals(goalState)) {
                    room.setReward(100);
                }
                
                rooms.add(room);
                
            }
        }
    }
    
    public void buildMaze() {
        BorderPane background = new BorderPane();
        ScrollPane sp = new ScrollPane();
        GridPane mapGrid = buildMazeGridRooms();
        
        sp.setContent(mapGrid);
        background.setCenter(sp);
        
        border.setCenter(background);
    }
        
    
    public GridPane buildMazeGridRooms() {
        
        for (QMazeRoom room: rooms) {
            int columnIndex = room.getCoordinates().getXCoordinate();
            int rowIndex = room.getCoordinates().getYCoordinate();
            Rectangle r = new Rectangle(50,50);
            Tooltip tp = new Tooltip("R:" + rowIndex + ", C:" + columnIndex);
            Tooltip.install(r, tp);
            boolean open = room.getOpen();
            boolean hasAgent = agentLocation != null && room.getCoordinates().equals(agentLocation);
            double visitCount = room.getWeightedVisitCount();
            if (room.getReward() > 0) {
                if (hasAgent) {
                    r.setFill(agentAtGoal);
                } else {
                    r.setFill(goal);
                }
            } else if (hasAgent) {
                r.setFill(agent);
            } else if (open) {
                r.setFill(getHeatMapColor(visitCount));
            } else {
                r.setFill(Color.DARKGRAY);
            }
        
            r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent value) {
                    boolean open = room.getOpen();
                    room.open(!open);
                    buildMaze();
                }
            });
    
            gridPane.add(r, columnIndex, rowIndex);
        }
        return gridPane;
    }
    
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
 
    public Coordinates getGoalState() {
        return goalState;
    }
    
    public Coordinates getAgentLocation() {
        return agentLocation;
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
    
    private void redrawMaze() {
        
        Node centreNode = border.getCenter();
        if (centreNode != null) {
            border.getChildren().remove(centreNode);
        }
        buildMaze();
        
    }

    private long getInterval(int stepsToGoal) {
        
        System.out.println("Steps are: " + stepsToGoal);
        long interval = QMazeFixedParams.ANIMATION_INTERVAL;
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
    
    private void setAgentLocation(Coordinates agentLocation) {
        this.agentLocation = agentLocation;
    }
    
    public void showVisitCount(HashMap<Coordinates, Integer> heatMap, String heatMapColor) {
    
        if (heatMap == null) {
            return;
        }
        this.heatMapColor = heatMapColor;
        //Get max visit count
        double maxVisit = getMaxVisit(heatMap);
        
        for (QMazeRoom room: rooms) {
            Coordinates roomLocation = room.getCoordinates();
            if (heatMap.containsKey(roomLocation)) {
                double roomVisits = heatMap.get(roomLocation);
                room.setWeightedVisitCount(roomVisits/maxVisit);
            }
        }
        redrawMaze();
    }
    
    private double getMaxVisit(HashMap<Coordinates, Integer> heatMap) {
        double maxVisit = 0;
        Set<Coordinates> keys = heatMap.keySet();
        for (Coordinates key: keys) {
            int value = heatMap.get(key);
            if (value > maxVisit) {
                maxVisit = value;
            }
        }
        return maxVisit;
    }
    

    private Paint getHeatMapColor(double visitCount) {
        switch(heatMapColor) {
            case("Green"):
                return Color.color((1-visitCount), 1, (1-visitCount));
            case("Yellow"):
                return Color.color(1, 1, (1-visitCount));
            case("Pink"):
                return Color.color(1, (1-visitCount), 1);
            case("Cyan"):
                return Color.color((1-visitCount), 1, 1);
            case("Blue"):
                return Color.color((1-visitCount), (1-visitCount), 1);
            case("Red"):
                return Color.color(1, (1-visitCount), (1-visitCount));    
            default:
                return Color.color(1, 1, 1);
        }
    }
}
