package qmaze.Agent;

import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class AgentTest {
    
    AgentLearningParameters learningParameters;
    
    public AgentTest() {
    }
    
    @Before
    public void setUp() {
        learningParameters = new AgentLearningParameters(0.1,0.1,0.7);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class Agent.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        Coordinates startingState = new Coordinates(0,0);
        Agent agent = new Agent(learningParameters);
        agent.start(startingState);
        assertEquals(startingState, agent.location());
    }

    /**
     * Test of chooseAction method, of class Agent.
     */
    @Test
    public void testChooseActionWhenOnlyOne() throws Exception {
        System.out.println("chooseAction");
        Coordinates nextAction = new Coordinates(1,0);
        ArrayList<Coordinates> nextAvailableActions = new ArrayList();
        nextAvailableActions.add(nextAction);
        Agent agent = new Agent(learningParameters);
        agent.start(new Coordinates(0,0));
        Coordinates result = agent.chooseAction(nextAvailableActions);
        assertEquals(nextAction, result);
    }
    
    /**
     * Test of chooseAction method, of class Agent.
     */
    @Test
    public void testChooseActionWhenTwo() throws Exception {

        Coordinates nextAction1 = new Coordinates(1,0);
        Coordinates nextAction2 = new Coordinates(0,1);
        ArrayList<Coordinates> nextAvailableActions = new ArrayList();
        nextAvailableActions.add(nextAction1);
        nextAvailableActions.add(nextAction2);
        Agent agent = new Agent(learningParameters);
        agent.start(new Coordinates(0,0));
        Coordinates result = agent.chooseAction(nextAvailableActions);
        assertTrue(result.equals(nextAction1) || result.equals(nextAction2));
    }

    /**
     * Test of takeAction method, of class Agent.
     */
    @Test
    public void testTakeAction() {
        //Going SS -> S1 -> GS
        //Going SS -> S2 -> GS
        
        //Then SS -> S1 -> GS
        //and SS -> S2 -> GS
        
        Coordinates startingState = new Coordinates(0,0);
        Coordinates stateOne = new Coordinates(0,1);
        Coordinates stateTwo = new Coordinates(1,0);
        Coordinates goalState = new Coordinates(1,1);
        
        Agent agent = new Agent(learningParameters);
        agent.start(startingState);
        AgentMemory memory = agent.getMemory();
        
        //Going SS -> S1 -> GS
        agent.takeAction(stateOne, 0);
        assertEquals(stateOne, agent.location());
        
        agent.takeAction(goalState, 100);
        assertEquals(goalState, agent.location());
        
        assertEquals(0, memory.rewardFromAction(startingState, stateOne), 0);
        assertEquals(10, memory.rewardFromAction(stateOne, goalState), 0);
        
        //Going SS -> S2 -> GS
        agent.start(startingState);
        agent.takeAction(stateTwo, 0);
        assertEquals(stateTwo, agent.location());
        agent.takeAction(goalState, 100);
        assertEquals(goalState, agent.location());
        
        memory.move(startingState);
        assertEquals(0, memory.rewardFromAction(startingState, stateTwo), 0);
        assertEquals(0, memory.rewardFromAction(startingState, stateOne), 0);
        memory.move(stateTwo);
        assertEquals(10, memory.rewardFromAction(stateTwo, goalState), 0);
        
        //Then SS -> S1 -> GS
        agent.start(startingState);
        agent.takeAction(stateOne, 0);
        assertEquals(stateOne, agent.location());
        agent.takeAction(goalState, 100);
        assertEquals(goalState, agent.location());
        assertEquals(0, memory.rewardFromAction(startingState, stateTwo), 0);
        assertEquals(0.7, memory.rewardFromAction(startingState, stateOne), 0.01);
        assertEquals(19, memory.rewardFromAction(stateOne, goalState), 0);
        
        //Then SS -> S2 -> GS
        agent.start(startingState);
        agent.takeAction(stateTwo, 0);
        assertEquals(stateTwo, agent.location());
        agent.takeAction(goalState, 100);
        assertEquals(goalState, agent.location());
        assertEquals(0.7, memory.rewardFromAction(startingState, stateTwo), 0.01);
        assertEquals(0.7, memory.rewardFromAction(startingState, stateOne), 0.01);

        assertEquals(19, memory.rewardFromAction(stateTwo, goalState), 0);
    }
    
}
