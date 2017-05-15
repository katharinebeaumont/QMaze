/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author katharine
 */
public class QLearning {
    
    private Environment environment;
    private QTable qTable;
    private Journey journey;
    private double epsilon;
    private double gamma;
    private int episodes;
    private int learningRate;
    
    public QLearning(Environment environment) {
        this.environment = environment;
        this.qTable = new QTable(environment);
    }
    
    public void startLearningWithDefaultValues() {        
        startLearning(0.2, 0.7, 10, 1);
    }

    public QTable getQTable() {
        return qTable;
    }

    public void printJourney() {
        System.out.println(journey.toString());
    }
    
    public void startLearning(double epsilon, double gamma, int episodes) {
        startLearning(epsilon, gamma, episodes, 1);
    }
    
    public void startLearning(double epsilon, double gamma, int episodes, int learningRate) {
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.episodes = episodes;
        this.learningRate = learningRate;
        this.journey = new Journey();
        
        for (int i=0; i<episodes; i++) {
            int step = 0;
            Pair currentRoom = environment.getStartingState();
            Pair goalState = environment.getGoalState();
            
            HashMap<Integer, Pair> journeyForEpisode = new HashMap();
            journeyForEpisode.put(step, currentRoom);
            
            while (!currentRoom.equals(goalState)) {
                Pair nextRoom;
                double exploreOrMemory = Math.random();
                if (exploreOrMemory < epsilon) {
                    nextRoom = qTable.getRandomSurroundingRoom(currentRoom);
                } else {
                    nextRoom = qTable.getBestSurroundingRoomOrRandom(currentRoom);
                }
                //Q learning: add the reward for moving from the current room, into the next one
                // From current state, to next action
                double rewardForNextRoom = environment.getRoom(nextRoom).getReward();
                // To the maximum reward we remember (in the Q table) that we could get, moving
                // onwards from the next action. 
                // Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all actions)]
                Pair bestRoomAfterNextRoom = qTable.getBestSurroundingRoomOrRandom(nextRoom);
                double rewardForBestRoomAfterNextRoom = environment.getRoom(bestRoomAfterNextRoom).getReward();
                double totalReward = rewardForNextRoom + (gamma * rewardForBestRoomAfterNextRoom);
                qTable.update(currentRoom, nextRoom, totalReward);
                
                //Move into the next room
                currentRoom = nextRoom;
                //Increment the steps
                step++;
                journeyForEpisode.put(step, currentRoom);
            }
            
            journey.put(i, journeyForEpisode);
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
        while (!currentRoom.equals(goalState)) {
            Pair nextRoom = qTable.getBestSurroundingRoomOrRandom(currentRoom);
            System.out.println("Next room: " + nextRoom.toString());
            currentRoom = nextRoom;
            //Increment the steps
            step++;
            journeyToOptimal.put(step, currentRoom);
        }
        return journeyToOptimal;
    }
}
