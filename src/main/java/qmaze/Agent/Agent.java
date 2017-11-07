package qmaze.Agent;

import java.util.ArrayList;
import qmaze.Environment.Coordinates;

/**
 * Q(S(t), A(t)) ← Q(S(t), A(t)) + α [ R(t+1) + γ max Q(S(t+1), a) − Q(S(t), A(t)) ].
 * 
 * @author katharine
 * I know about:
 *  - My memory of learned rewards and possible actions
 *  - My learning parameters
 * I am told about:
 *  - The surrounding open rooms.
 *  - If there is a reward in this room.
 *   and use them to make decisions about which room to go in next.
 * I don't know:
 *  - How many episodes I am trained for
 * I don't control:
 *  - My movements overall - instead I am told to move at each step
 *  and given information about the environment.
 */
public class Agent {
    
    private final AgentMemory memory;
    private AgentLearningParameters learningParameters;
    
    public Agent(AgentLearningParameters learningParameters) {
        this.learningParameters = learningParameters;
        this.memory = new AgentMemory();
    }
    
    public Coordinates location() {
        return memory.getCurrentState();
    }
    
    public void start(Coordinates startingState) {
        memory.setStartingState(startingState);
    }
    
    public void move(Coordinates nextState) {
        memory.move(nextState);
    }
        
    public Coordinates chooseAction(ArrayList<Coordinates> nextAvailableActions) throws NoWhereToGoException {
         //What if there are no available actions?
        // Should this validation happen further up...
        if (nextAvailableActions.isEmpty()) {
            throw new NoWhereToGoException(memory.getCurrentState());
        }
        
        //CODE TO SELECT NEXT ACTION
        Coordinates nextAction;
        
        throw new RuntimeException("IMPLEMENT ME!");
    }
    
    public void takeAction(Coordinates actionTaken, double reward) {
                
        double qValue = 0; //TO WORK OUT!!!!
        memory.updateMemory(actionTaken, qValue);
        memory.move(actionTaken);
        throw new RuntimeException("IMPLEMENT ME!");
    }
    
    
    public AgentMemory getMemory() {
        return memory;
    }
    
    public AgentLearningParameters getLearningParameters() {
        return learningParameters;
    }
    
    public void setLearningParameters(AgentLearningParameters parameters) {
        this.learningParameters = parameters;
    }
    
    public void introduceSelf(Coordinates startingState) {
        double alpha = learningParameters.getLearningRate();
        double gamma = learningParameters.getGamma();
        double epsilon = learningParameters.getEpsilon();
        System.out.println("I'm training with epsilon: " + epsilon + " gamma: " 
                + gamma + " and alpha: " + alpha + "\nStaring at " + startingState.toString());
    }
    
}
