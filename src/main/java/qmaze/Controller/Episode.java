package qmaze.Controller;

import java.util.ArrayList;
import qmaze.Agent.Agent;
import qmaze.Agent.NoWhereToGoException;
import qmaze.Environment.Coordinates;
import qmaze.Environment.Maze;

/**
 *
 * @author katharine
 * The events that join the agent with the environment
 */
public class Episode {
    
    public final Agent agent;
    public final Maze maze;
    public final int STEP_LIMIT = 5000;
    
    public final Coordinates startingState;
    
    public ArrayList<Coordinates> episodeSteps;
    
    public Episode(Agent agent, Maze maze, Coordinates startingState) {
        this.agent = agent;
        this.maze = maze;
        this.startingState = startingState;
        this.episodeSteps = new ArrayList();
    }
    
    public void play() throws EpisodeInterruptedException {
        
        agent.start(startingState);
        episodeSteps.add(startingState);
        while(!atGoalState()) {
            Coordinates action = getNextAction();
            
            //Did the maze give a reward?
            double reward = maze.getReward(action);
            agent.takeAction(action, reward);
            
            recordSteps(action);
        }
        System.out.println("Finished episode with " + episodeSteps.size() + " steps.");
    }

    public ArrayList<Coordinates> getEpisodeSteps() {
        return episodeSteps;
    }
    
    public void recordSteps(Coordinates action) throws EpisodeInterruptedException {
        episodeSteps.add(action);
        if (episodeSteps.size() == STEP_LIMIT) {
            throw new EpisodeInterruptedException("taking too long!", episodeSteps.size());
        }
    }

    public Coordinates getNextAction() throws EpisodeInterruptedException {
        //Where is the agent?
        Coordinates currentState = agent.location();
        //Have a look around the maze
        ArrayList<Coordinates> adjoiningStates = maze.getAdjoiningStates(currentState);
        //Decide on action
        Coordinates action;
        try {
            action = agent.chooseAction(adjoiningStates);
        } catch (NoWhereToGoException e) {
            throw new EpisodeInterruptedException(e, episodeSteps.size());
        }
        return action;
    }
    
    public boolean atGoalState() {
        return maze.isGoalState(agent.location());
    }  
}
