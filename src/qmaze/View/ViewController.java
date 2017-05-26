package qmaze.View;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.Pane;
import qmaze.Controller.LearningController;
import qmaze.Environment.Coordinates;
import qmaze.View.Components.Component;
import qmaze.View.MazeComponents.QMazeGrid;

/**
 *
 * @author katharine
 * 2 types of component: animated and non-animated.
 *  - The non-animated (e.g. buttons, sliders, Q Learning panel) are all treated the same.
 *  - The animated (maze) is a special case, built by the controller and directly managed.
 * The ComponentController:
 *  - Acts as a go between for LearningController and components
 *  - Manages state across the Components.
 */
public class ViewController {
    
    private final LearningController learningController;
    public String STATE;

    private TrainingConfig config;
    private String heatMapColour = "None";
    
    private ArrayList<Component> components;
    private QMazeGrid maze;
    
    public ViewController() {
        this.STATE = Component.RESET_STATE;
        learningController = new LearningController();  
        components = new ArrayList();
    }
    
    public void register(Component component) {
        components.add(component);
    }
    
    public String getHeatMapColour() {
        return heatMapColour;
    }
        
    public TrainingConfig getQMazeConfig() {
        return config;
    }
    
    public void learningParameterReset(TrainingConfig config) {
        this.config = config;
        this.STATE = Component.ADJUST_PARAM_STATE;
        reset();
    }
    
    public void heatMapReset(String heatMapColour) {
        this.heatMapColour = heatMapColour;
        this.STATE = Component.TRAINED_STATE;
        reset();
    }
        
    private void reset() {
        components.forEach((c) -> {
            c.reset();
        });
    }
    
    public void hardReset() {
        this.STATE = Component.RESET_STATE;
        reset();
    }
    
    public void roomReset() {
        this.STATE = Component.ADJUST_MAZE_STATE;
        reset();
    }

    public void startTraining() {
        this.STATE = Component.TRAINED_STATE;
        System.out.println("Training");
        learningController.startLearning(maze.getRooms(), maze.getRows(), maze.getColumns(), maze.getStartingState(), config);
        reset();
    }
    
    public HashMap<Coordinates, Integer> getHeatMap() {
         return learningController.getHeatMap();
    }
    
    public void showOptimalPath() {
        System.out.println("Finding optimal path...");
        ArrayList<Coordinates> optimalPath = learningController.getOptimalPath(maze.getStartingState());
        maze.animateMap(optimalPath);
    }

    public Coordinates getGoalState() {
        if (maze == null) {
            return null;
        }
        return maze.getGoalState();
    }

    public HashMap<Coordinates, HashMap<Coordinates, Double>> getLearnings() {
        if (maze == null) {
            return new HashMap();
        }
        return learningController.getLearnings(maze.getRooms());
    }

    public Pane getMaze() {
        maze = new QMazeGrid(this);
        return maze.build();
    }
    
}
