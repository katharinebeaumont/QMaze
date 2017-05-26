package qmaze.Agent;

import java.util.ArrayList;
import org.junit.Test;

import static org.junit.Assert.*;
import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class AgentMemoryTest {
    
    public AgentMemoryTest() {
    }
    
    /**
     * Test of updateMemory method, of class AgentMemory.
     */
    @Test
    public void testUpdateMemory() {
        
        Coordinates currentState = new Coordinates(0,0);
        Coordinates nextState = new Coordinates(0,1);
        Coordinates anotherState = new Coordinates(1,0);
        
        int reward = 0;
        AgentMemory memory = new AgentMemory();
        memory.setStartingState(currentState);
        //Update memory when there is none
        
        memory.updateMemory(nextState, reward);
        assertEquals(0, memory.rewardFromAction(currentState, nextState), 0);

        //Now update when there is some
        int increasedReward = 5;
        memory.updateMemory(nextState, increasedReward);
        assertEquals(increasedReward, memory.rewardFromAction(currentState, nextState), 0);
        //And increase it again
        memory.updateMemory(nextState, increasedReward);
        assertEquals((increasedReward + increasedReward), memory.rewardFromAction(currentState, nextState), 0);
        
        //Try another unknown memory
        memory.updateMemory(anotherState, increasedReward);
        assertEquals((increasedReward ), memory.rewardFromAction(currentState, anotherState), 0);
        
        //And a new room
        memory.move(anotherState);
        memory.updateMemory(currentState, increasedReward);
        assertEquals((increasedReward ), memory.rewardFromAction(anotherState, currentState), 0);
        
    }
    
    @Test
    public void testBuildUpMemory() {
        //Move from SS -> S1 -> SS -> S2 -> GS
        //Should get memory: SS : S1,0 S2,0
        //S1 : SS,0
        //S2 : GS,100
        
        Coordinates startingState = new Coordinates(0,0);
        Coordinates stateOne = new Coordinates(0,1);
        Coordinates stateTwo = new Coordinates(1,0);
        Coordinates goalState = new Coordinates(1,0);
        
        //SS : S1,0
        AgentMemory memory = new AgentMemory();
        memory.setStartingState(startingState);
        ArrayList actionsFromStartingState = memory.actionsForState(startingState);
        assertTrue(actionsFromStartingState.isEmpty());
        memory.updateMemory(stateOne, 0);
        actionsFromStartingState = memory.actionsForState(startingState);
        assertTrue(actionsFromStartingState.contains(stateOne));
        assertEquals(1, actionsFromStartingState.size());
        
        //S1 : SS,0
        assertEquals(0, memory.rewardFromAction(startingState, stateOne), 0);
        memory.move(stateOne);
        ArrayList actionsFromStateOne = memory.actionsForState(stateOne);
        assertTrue(actionsFromStateOne.isEmpty());
        memory.updateMemory(startingState, 0);
        actionsFromStateOne = memory.actionsForState(stateOne);
        assertTrue(actionsFromStateOne.contains(startingState));
        assertEquals(1, actionsFromStateOne.size());
        
        //SS : S1,0 S2,0
        assertEquals(0, memory.rewardFromAction(stateOne, startingState), 0);
        memory.move(startingState);
        actionsFromStartingState = memory.actionsForState(startingState);
        assertTrue(actionsFromStartingState.contains(stateOne));
        memory.updateMemory(stateTwo, 0);
        actionsFromStartingState = memory.actionsForState(startingState);
        assertTrue(actionsFromStartingState.contains(stateTwo));
        assertEquals(2, actionsFromStartingState.size());
        assertEquals(0, memory.rewardFromAction(startingState, stateTwo), 0);
        memory.move(stateTwo);
        
        //S2 : GS,100
        ArrayList actionsFromStateTwo = memory.actionsForState(stateTwo);
        assertTrue(actionsFromStateTwo.isEmpty());
        memory.updateMemory(goalState, 100);
        actionsFromStateTwo = memory.actionsForState(stateTwo);
        assertTrue(actionsFromStateTwo.contains(goalState));
        assertEquals(1, actionsFromStateTwo.size());
        assertEquals(100, memory.rewardFromAction(stateTwo, goalState), 0);
    }

    
}
