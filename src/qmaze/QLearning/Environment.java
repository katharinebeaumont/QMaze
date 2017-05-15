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
 *  to the outside world. The inner edges can be open or closed to 
 *  make up the maze, so R1 could be open on the right to R2 
 *  (and so R2 on the left is open). The final room has the reward.
 */
public class Environment {
    
    private HashMap<Pair,Room> rooms;
    private int columns;
    private int rows;
    
    //TODO: shouldn't be able to call default constructor
    // as methods will return null.
    public Environment() {
        
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
        return new Pair(rows-1,columns-1);
    }
    
    public Pair getStartingState() {
        return new Pair(0,0);
    }
    
    public Room getRoom(Pair coordinate) {
        return rooms.get(coordinate);
    }
        
    public void setDefaultMaze(){
        
        columns = 5;
        rows = 7;
        rooms = new HashMap<>();
        //Row by row create rooms
        for (int row=0; row<rows; row++) {
            for (int col=0; col<columns; col++) {
                Pair coordinates = new Pair(row,col);
                Room room = new Room();
                if (row==0 && col==0) {
                    room.setHasAgent(true);
                }
                if (row==rows-1 && col==columns-1) {
                    room.setReward(100);
                }
                rooms.put(coordinates,room);
            }
            
        }

    }
    
    public void setSmallerDefaultMaze(){
        
        columns = 4;
        rows = 4;
        rooms = new HashMap<>();
        //Row by row create rooms
        for (int row=0; row<rows; row++) {
            for (int col=0; col<columns; col++) {
                Pair coordinates = new Pair(row,col);
                Room room = new Room();
                if (row==0 && col==0) {
                    room.setHasAgent(true);
                }
                if (row==rows-1 && col==columns-1) {
                    room.setReward(100);
                }
                rooms.put(coordinates,room);
            }
            
        }

    }
}
