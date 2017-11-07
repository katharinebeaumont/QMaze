package qmaze.Controller;

import java.util.ArrayList;
import qmaze.Agent.Agent;
import qmaze.Agent.AgentLearningParameters;
import qmaze.Environment.Coordinates;
import qmaze.Environment.Maze;

/**
 *
 * @author katharine
 */
public class OptimalEpisode extends Episode {
    
    public OptimalEpisode(Agent agent, Maze maze, Coordinates startingState) {
        super(agent,maze,startingState);
    }
    
    public ArrayList findOptimalPath() throws EpisodeInterruptedException {
        
        AgentLearningParameters originalLearningParameters = agent.getLearningParameters();
        haltExploration(originalLearningParameters);
        
        agent.start(startingState);
        episodeSteps.add(startingState);
        while(!atGoalState()) {
            Coordinates action = getNextAction();
            agent.move(action);
            recordSteps(action);
            System.out.println("Moved to " + action.toString());
        }
        System.out.println("Found optimalPath in " + episodeSteps.size() + " steps.");
        
        resumeExploration(originalLearningParameters);
        return episodeSteps;      
    }

    private void haltExploration(AgentLearningParameters originalLearningParameters) {
        
        AgentLearningParameters noExploreLearningParameters = new AgentLearningParameters(0,
                originalLearningParameters.getLearningRate(), originalLearningParameters.getGamma());
        agent.setLearningParameters(noExploreLearningParameters);
    }
    
    private void resumeExploration(AgentLearningParameters originalLearningParameters) {
        
        agent.setLearningParameters(originalLearningParameters);
    }
    
}
