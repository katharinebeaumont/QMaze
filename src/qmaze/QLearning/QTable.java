/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

import java.util.HashMap;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author katharine
 * This needs to map: the current state, to the memory of rewards at the next action.
 * So we need: Pair of co-ordinates (current state), mapped to a HashMap of next state
 * (co-ordinate Pair again) to int reward value.
 */
public class QTable {
    private HashMap<Pair,HashMap<Pair,Double>> qTable;

    QTable(Environment environment) {
        qTable = new HashMap<>();
        HashMap<Pair,Room> rooms = environment.getRooms();
        int height = environment.getRows();
        int width = environment.getColumns();
        double initialReward = 0;
        
        //Go through the keySet (all the room locations).
        //Each room location forms the key of the QTable.
        Set<Pair> keys = rooms.keySet();
        for (Pair roomCoordinate: keys) {
            //For each, work out the neighbouring rooms that are open.
            //Does it have a room above it?
            HashMap<Pair,Double> surroundingRooms = new HashMap();
            int row = (int)roomCoordinate.getKey();
            int column = (int)roomCoordinate.getValue();
            if (row > 0) {
                //Then there is a room above
                Pair roomAbove = new Pair(row-1,column);
                Room r = rooms.get(roomAbove);
                if (r.getOpen()) {
                    surroundingRooms.put(roomAbove, initialReward);
                }
            }
            //Does it have a room below it?
            if (row < height-1) {
                //Then there is a room below
                Pair roomBelow = new Pair(row+1,column);
                Room r = rooms.get(roomBelow);
                if (r.getOpen()) {
                    surroundingRooms.put(roomBelow, initialReward);
                }
            }
            //Does it have a room to the left?
            if (column > 0) {
                Pair roomLeft = new Pair(row,column-1); 
                Room r = rooms.get(roomLeft);
                if (r.getOpen()) {
                    surroundingRooms.put(roomLeft, initialReward);
                }
            }
            //Does it have a room to the right?
            if (column < width-1) {
                Pair roomRight = new Pair(row,column+1); 
                Room r = rooms.get(roomRight);
                if (r.getOpen()) {
                    surroundingRooms.put(roomRight, initialReward);
                }
            }
            qTable.put(roomCoordinate, surroundingRooms);
        }
    }
    
    public HashMap<Pair,Double> getSurroundingRooms(Pair room) {
        return qTable.get(room);
    }

    public Pair getRandomSurroundingRoom(Pair room) {
        HashMap<Pair,Double> surroundingRooms = qTable.get(room);
        Set roomLocations = surroundingRooms.keySet();
        int size = roomLocations.size();
        int randomChoice = (int)(Math.random() * size);
        Object[] locations = roomLocations.toArray();
        Pair choice = (Pair)locations[randomChoice];
        return choice;
    }
        
    public Pair getBestSurroundingRoomOrRandom(Pair room) {
        HashMap<Pair,Double> surroundingRooms = qTable.get(room);
        Pair bestPair = null;
        double highestReward = 0;
        
        for(HashMap.Entry<Pair,Double> entry : surroundingRooms.entrySet()) {
            Pair p = entry.getKey();
            if (bestPair == null) {
                bestPair = p;
            }
            double reward = entry.getValue();
            if (reward > highestReward) {
                highestReward = reward;
                bestPair = p;
            }
        }
        if (highestReward == 0) {
            return getRandomSurroundingRoom(room);
        }
        return bestPair;           
    }
    
    public void update(Pair currentState, Pair nextAction, double reward) {
        HashMap<Pair,Double> surroundingRooms = getSurroundingRooms(currentState);
        double currentReward = surroundingRooms.get(nextAction);
        double updatedReward = currentReward + reward;
        surroundingRooms.put(nextAction, updatedReward);
    }
    
    public HashMap getTable() {
        return qTable;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //TODO: print this nicely.
        Set<Pair> keys = qTable.keySet();
        for (Pair coordinate: keys) {
            sb.append("For room: ");
            sb.append(coordinate.getKey());
            sb.append(",");
            sb.append(coordinate.getValue());
            sb.append(" the surrounding rooms are: ");
            HashMap surroundingRooms = qTable.get(coordinate);
            Set<Pair> otherRoomKeys = surroundingRooms.keySet();
            for (Pair surroundingRoomCoordinate: otherRoomKeys) {
                sb.append("\n");
                sb.append(surroundingRoomCoordinate.getKey());
                sb.append(",");
                sb.append(surroundingRoomCoordinate.getValue());
                sb.append(" with reward: ");
                double reward = (double)surroundingRooms.get(surroundingRoomCoordinate);
                sb.append(reward);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
