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
 * This is a rectangular space where each unit is a 'room'. 
 * Think of it as a matrix: 
 * e.g. width and height are 3,4, so get the following rooms:
 *  R1  R2  R3
 *  R4  R5  R6
 *  R7  R8  R9
 *  R10 R11 R12
 * 
 *  Each room has boundaries at the top, bottom, left and right.
 *  The rooms on the outer edges (R1-3, 4, 6, 7, 9-12) are closed 
 *  to the outside world. 
 *  The rooms can be open or closed, configured by clicking on the map.
 */
public class Environment {
    
    private HashMap<Pair,Room> rooms;
    private final int columns;
    private final int rows;
    private Pair startingState;
    private Pair goalState;
    
    public Environment(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        startingState = new Pair(0,0);
        goalState = new Pair(rows-1,columns-1);
        setupRooms();
    }
    
    public int getColumns() {
        return columns;
    }
    public int getRows() {
        return rows;
    }
    public HashMap getRooms() {
        return rooms;
    }
    
    public Pair getGoalState() {
        return goalState;
    }
    
    public Pair getStartingState() {
        return startingState;
    }
    
    public void setGoalState(Pair goalState) {
        if (startingState.equals(goalState)) {
            System.out.println("Cant have same starting room as goal");
            return;
        }
        this.goalState = goalState;
    }
    
    public void setStartingState(Pair startingState) {
        if (startingState.equals(goalState)) {
            System.out.println("Cant have same starting room as goal");
            return;
        }
        this.startingState = startingState;
    }
    
    public void resetAgent() {
        Room startingRoom = getRoom(startingState);
        startingRoom.setHasAgent(true);
        Room goalRoom = getRoom(goalState);
        goalRoom.setHasAgent(false);
    }
    
    public Room getRoom(Pair coordinate) {
        return rooms.get(coordinate);
    }

    private void setupRooms() {
        rooms = new HashMap<>();
                
        //Row by row create rooms
        for (int row=0; row<rows; row++) {
            for (int col=0; col<columns; col++) {
                Pair coordinates = new Pair(row,col);
                Room room = new Room();
                
                if (row==(int)startingState.getKey() && col==(int)startingState.getValue()) {
                    room.setHasAgent(true);
                }
                if (row==(int)goalState.getKey() && col==(int)goalState.getValue()) {
                    room.setReward(100);
                }
                rooms.put(coordinates,room);
            }
            
        }
    }
}
