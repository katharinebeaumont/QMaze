package qmaze.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import qmaze.Agent.Agent;
import qmaze.Agent.AgentLearningParameters;
import qmaze.Agent.AgentMemory;
import qmaze.Environment.Coordinates;
import qmaze.Environment.Maze;
import qmaze.View.TrainingConfig;
import qmaze.View.MazeComponents.QMazeRoom;

/**
 *
 * @author katharine
 * I know about things I get from the view:
 *  - Learning parameters
 *  - Number of episodes
 *  - The size of the maze
 *  - If maze rooms are open or closed
 * I use the view data to configure the model.
 * I get these back from the model:
 *  - Q values 
 *  - Optimal path
 *  - Journey data
 * I pass information from the model to the view.
 */
public class LearningController {

    private Maze maze;
    private Agent agent;
    private AgentLearningParameters learningParameters;
    private HashMap<Coordinates, Integer> heatMap;
    private static final int EXCEPTION_THRESHOLD = 20;
    
    public LearningController() {
        
    }
    
    public void startLearning(ArrayList<QMazeRoom> rooms, int rows, int columns, Coordinates startingState, TrainingConfig mazeConfig) throws TrainingInterruptedException {
        
        int exceptionCount = 0;
        heatMap = new HashMap();
        initialiseMaze(rooms, rows, columns);
        initialiseAgent(mazeConfig);
        agent.introduceSelf(startingState);
        int episodes = mazeConfig.getEpisodes();
        
        for (int i=0; i<episodes; i++) {
            
            System.out.println("**Training episode " + i);
            Episode e = new Episode(agent, maze, startingState);
            try { 
                e.play();
            } catch (EpisodeInterruptedException ex) {
                System.out.println(ex.getMessage());
                exceptionCount++;
                if (exceptionCount > EXCEPTION_THRESHOLD) {
                    throw new TrainingInterruptedException("I've exceeded the failure threshold.");
                }
            }
            buildHeatMap(e.getEpisodeSteps());
        }
    }
    
    public void initialiseMaze(ArrayList<QMazeRoom> rooms, int rows, int columns) {
        maze = new Maze(rows, columns);
        rooms.forEach((r) -> {
            Coordinates roomLocation = new Coordinates(r.getCoordinates());
            boolean open = r.getOpen();
            maze.setOpen(roomLocation, open);
            if (r.getReward() > 0) {
                //TODO: make configurable, so more that one room can have a reward
                // So need a different way of signifying goal state
                maze.setGoalState(roomLocation, r.getReward());
            }
        });
    }

    private void initialiseAgent(TrainingConfig mazeConfig) {
        learningParameters = new AgentLearningParameters(mazeConfig.getEpsilon(), mazeConfig.getAlpha(), mazeConfig.getGamma());
        agent = new Agent(learningParameters);
    }
      
    public HashMap<Coordinates, HashMap<Coordinates,Double>> getLearnings(ArrayList<QMazeRoom> rooms) {
         
        HashMap<Coordinates, HashMap<Coordinates,Double>> learning = new HashMap();
        if (agent == null) {
            return learning;
        }
        AgentMemory memory = agent.getMemory();
        
        rooms.forEach((r) -> {
            Coordinates roomLocation = r.getCoordinates();
            if (r.getOpen()) {
                HashMap<Coordinates,Double> rewardFromRoom = new HashMap();
                ArrayList<Coordinates> potentialActions = memory.actionsForState(roomLocation);
                for (Coordinates action: potentialActions) {
                    double reward = memory.rewardFromAction(roomLocation, action);
                    rewardFromRoom.put(action, reward);
                }
                learning.put(roomLocation, rewardFromRoom);
            }
        });
        return learning;
    }
    
    public ArrayList<Coordinates> getOptimalPath(Coordinates startingState) {
        OptimalEpisode e = new OptimalEpisode(agent, maze, startingState);
        ArrayList<Coordinates> optimalPath = new ArrayList();
        
        try { 
            optimalPath = e.findOptimalPath();
        } catch (EpisodeInterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        
        return optimalPath;
    }

    private void buildHeatMap(ArrayList<Coordinates> episodeSteps) {
        for (Coordinates roomVisited: episodeSteps) {
            
            Integer roomVisitCount = heatMap.get(roomVisited);
            if (roomVisitCount == null) {
                roomVisitCount = 0;
            }
            roomVisitCount++;
            heatMap.put(roomVisited, roomVisitCount);
        }
    }
    
    public HashMap<Coordinates, Integer> getHeatMap() {
        return heatMap;
    }
}
