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
        
        Coordinates nextAction;
        double useMemory = Math.random();
        if (useMemory < learningParameters.getEpsilon()) {
            nextAction = pickRandomAction(nextAvailableActions);
        } else {
            //Use learned values, but if there are none, it has to be random.
            nextAction = pickBestActionOrRandom(nextAvailableActions);
        }
        
        return nextAction;
    }
    
    public void takeAction(Coordinates actionTaken, double reward) {
                
        //Q(state, action) = R(state, action) + alpha * (Gamma * Max[Q(next state, all actions)] -  R(state, action))
        
        //R(state, action) 
        double currentQValue = memory.rewardFromAction(location(), actionTaken);
        
        //Max[Q(next state, all actions)]
        double estimatedBestFutureReward = 0;
        ArrayList<Coordinates> actionsForFutureState = memory.actionsForState(actionTaken);
        if (!actionsForFutureState.isEmpty()) {
            Coordinates max_reward_from_subequent_action = pickBestActionOrRandom(actionsForFutureState);
            estimatedBestFutureReward = memory.rewardFromAction(actionTaken, max_reward_from_subequent_action);
        }    
        
        double alpha = learningParameters.getLearningRate();
        double gamma = learningParameters.getGamma();
        //alpha * (Gamma * Max[Q(next state, all actions)] -  R(state, action))
        double qValue = (alpha * (reward + (gamma * estimatedBestFutureReward) - currentQValue));
        //Adding R(state, action) when we update
        memory.updateMemory(actionTaken, qValue);
        memory.move(actionTaken);
    }
    
    private Coordinates pickRandomAction(ArrayList<Coordinates> actions) {
        int options = actions.size();
        int choice = (int)(Math.random() * options);
        return actions.get(choice);
    }
    
    private Coordinates pickBestActionOrRandom(ArrayList<Coordinates> actions) {
        //There might be more than one best action
        ArrayList<Coordinates> bestActions = new ArrayList();
        double highestReward = 0;
        for (Coordinates action: actions) {
            double rewardMemory = memory.rewardFromAction(location(), action);
            if (rewardMemory > highestReward) {
                //Clear out any previous candidates for best action
                highestReward = rewardMemory;
                bestActions = new ArrayList();
                bestActions.add(action);
            }
            if (rewardMemory == highestReward) {
                //This means that bestActions could just contain zero reward actions,
                // in which case we're behaving the same as pickRandomAction(actions).
                bestActions.add(action);
            }
        }
        return pickRandomAction(bestActions);
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
