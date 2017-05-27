package qmaze.Controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import qmaze.Agent.Agent;
import qmaze.Agent.AgentLearningParameters;
import qmaze.Environment.Coordinates;
import qmaze.Environment.Maze;

/**
 *
 * @author katharine
 * This is really just hear so I can debug it and test manually.
 */
public class EpisodeTest {
    
    private Agent agent;
    private AgentLearningParameters agentLP;
    private Maze maze;
    
    private final Coordinates startingState = new Coordinates(0,0);
    private final int maze_size = 4;
    private final Coordinates goalState = new Coordinates(maze_size-1,maze_size-1);
    
    
    public EpisodeTest() {
    }
    
    @Before
    public void setUp() {
        agentLP = new AgentLearningParameters(0.1,0.1,0.1);
        agent = new Agent(agentLP);
        maze = new Maze(maze_size,maze_size);
        maze.setGoalState(goalState, 100);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of play method, of class Episode.
     */
    @Test
    public void testPlay() throws Exception {
        Episode e = new Episode(agent, maze, startingState);
        e.play();
        
    }
    
}
