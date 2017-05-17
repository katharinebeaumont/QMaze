/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author katharine
 */
public class QLearning {
    
    private final Environment environment;
    private final QLearningConfig config;
    private final QTable qTable;
    private final Journey journey;
    private static final int TRAINING_CUT_OFF = 10000;
    private static final int OPTIMAL_PATH_CUT_OFF = 5000;
    
    public QLearning(Environment environment, QLearningConfig config) {
        this.environment = environment;
        this.config = config;
        this.qTable = new QTable(environment);
        this.journey = new Journey();
    }
    
    public QTable getQTable() {
        return qTable;
    }

    public void printJourney() {
        System.out.println(journey.toString());
    }
    
    public void startLearning() {
        double epsilon = config.getEpsilon();
        double gamma = config.getGamma();
        int episodes = config.getEpisodes();
        double alpha = config.getAlpha();
        
        System.out.println("Starting with " + config.toString());
        for (int i=0; i<episodes; i++) {
            int step = 0;
            Pair currentState = environment.getStartingState();
            Pair goalState = environment.getGoalState();
            
            HashMap<Integer, Pair> journeyForEpisode = new HashMap();
            journeyForEpisode.put(step, currentState);
            
            while (!currentState.equals(goalState) && step < TRAINING_CUT_OFF) {
                Pair nextRoom;
                double exploreOrMemory = Math.random();
                if (exploreOrMemory < epsilon) {
                    nextRoom = qTable.getRandomSurroundingRoom(currentState);
                } else {
                    nextRoom = qTable.getBestSurroundingRoomOrRandom(currentState);
                }
                if (nextRoom == null) {
                    System.out.println("****WOAH!****");
                    System.out.println("Are you trying to blow this up?!");
                    break;
                }
                //Q learning: get the reward for moving from the current room, into the next one.
                // From current state, to next action. The reward comes from the environment.
                double rewardForNextRoom = environment.getRoom(nextRoom).getReward();
                
                // Add that to the maximum reward we remember (in the Q table) that we could get, moving
                // onwards from the next action. What do we remember about going forwards from the next action?
                // Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all actions)]
                Pair bestRoomAfterNextRoom = qTable.getBestSurroundingRoomOrRandom(nextRoom);
                double rewardForBestRoomAfterNextRoom = qTable.getQValue(nextRoom, bestRoomAfterNextRoom);
                double totalReward = rewardForNextRoom + (alpha * (gamma * rewardForBestRoomAfterNextRoom));
                qTable.update(currentState, nextRoom, totalReward);
                
                //Move into the next room
                currentState = nextRoom;
                //Increment the steps
                step++;
                journeyForEpisode.put(step, currentState);
            }
            if (step == TRAINING_CUT_OFF) {
                System.out.println("****WOAH!****");
                System.out.println("Are you trying to blow this up?!");
            }
            
            journey.put(i, journeyForEpisode);
            if (i % 10 == 0) {
                System.out.println("Episode " + i + " complete.");
            }
        }
        System.out.println("Learning complete.");
    }

    public HashMap<Integer,Pair> findOptimalPath() {
        Pair currentRoom = environment.getStartingState();
        Pair goalState = environment.getGoalState();
        HashMap<Integer,Pair> journeyToOptimal = new HashMap();
        int step = 0;
        journeyToOptimal.put(step, currentRoom);
        System.out.println("Figuring out journey.");
        System.out.println("Starting at: " + currentRoom.toString());
        while (!currentRoom.equals(goalState) && step < OPTIMAL_PATH_CUT_OFF) {
            Pair nextRoom = qTable.getBestSurroundingRoomOrRandom(currentRoom);
            currentRoom = nextRoom;
            //Increment the steps
            step++;
            journeyToOptimal.put(step, currentRoom);
        }
        if (step == OPTIMAL_PATH_CUT_OFF) {
            System.out.println("*****WOAH!*****");
            System.out.println("Stopped looking for optimal path, as I took 1000 steps.");
            System.out.println("Check your learning rate, and probability of exploring.");
            return new HashMap();
        }
        return journeyToOptimal;
    }
    
    public double getVisitTraffic(Pair coordinates) {
        Integer visit = journey.getVisit().get(coordinates);
        if (visit == null) {
            return 0;
        }
        System.out.println("visit is " + visit);
        System.out.println("max visits is " + journey.getMaxVisits());
        return (double)visit/(double)journey.getMaxVisits();
    }
}
