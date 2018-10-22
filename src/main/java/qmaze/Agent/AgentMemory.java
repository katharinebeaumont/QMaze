package qmaze.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import qmaze.Environment.Coordinates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author katharine
 * The Agent learns as it moves through the maze:
 *  - What room am I in? Co-ordinates.
 *  - Is there a reward for moving into this room?
 * Then it can recall:
 *  - What was the reward for moving into this room?
 *  - What are the best rooms I remember, the next step on?
 * 
 * Have to: initialise class
 *  then set starting state before anything else can happen.
 *  Why not do this in the constructor? We use the memory for multiple episodes.
 */
@NoArgsConstructor
public class AgentMemory {
    
    private HashMap<Coordinates, HashMap<Coordinates, Double>> mazeMemory = new HashMap<>();

    @Setter
    @Getter
    private Coordinates currentState;
    
    public void updateMemory(Coordinates action, double reward) {
        //Have I been in this room before?
        HashMap<Coordinates, Double> nextSteps = mazeMemory.get(currentState);
        //Nope, create blank slate
        if (nextSteps == null) {
            nextSteps = new HashMap<>();
        }
        
        //Have I checked what's in the next rooms
        Double rewardMemory = (Double)nextSteps.get(action);
        //Nope, blank slate
        if (rewardMemory == null) {
            rewardMemory = (double) 0;
        }
        
        //Store memory
        nextSteps.put(action, (rewardMemory + reward));
        mazeMemory.put(currentState, nextSteps);  
    }
    
    void move(Coordinates action) {
        this.currentState = action;
    }
    
    //What do I remember about future actions>
    public ArrayList<Coordinates> actionsForState(Coordinates state) {
        HashMap<Coordinates, Double> nextSteps = mazeMemory.get(state);
        if (nextSteps == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(nextSteps.keySet());
    }
    
    public double rewardFromAction(Coordinates state, Coordinates action) {
        HashMap nextSteps = mazeMemory.get(state);
        //Nope, no memory of next steps
        if (nextSteps == null) {
            return 0;
        }
       
        Double rewardMemory = (Double)nextSteps.get(action);
        //Nope, no memory of moving into this room.
        if (rewardMemory == null) {
            return 0;
        }
        return rewardMemory;
    }
}
