/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.Environment;

/**
 *
 * @author katharine
 * A Room knows:
 *  - Where it is
 *  - If it is open or closed
 *  - What reward (if any) it contains
 *  - If another room is adjoining/ neighboring
 * The Room does not know:
 *  - About the agent
 *  - About the maze
 */
public class Room {
    
    private boolean open = true;
    private double reward = 0;
    private final int x_coordinate;
    private final int y_coordinate;
    
    public Room(int x_coordinate, int y_coordinate) {
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
    }
    
    public Room(Coordinates coordinates) {
        this.x_coordinate = coordinates.getXCoordinate();
        this.y_coordinate = coordinates.getYCoordinate();
    }

    public void open(boolean open) {
        this.open = open;
    }
    
    public boolean isOpen() {
        return open;
    }
    
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    } 
    
    public int getXCoordinate() {
        return x_coordinate;
    }
    
    public int getYCoordinate() {
        return y_coordinate;
    }
        
    public boolean hasSameLocation(Room otherRoom) {
        return otherRoom.getXCoordinate() == x_coordinate && otherRoom.getYCoordinate() == y_coordinate;
    }
    
    public boolean adjoins(Room otherRoom) {
        int x_other = otherRoom.getXCoordinate();
        int y_other = otherRoom.getYCoordinate();
        return (x_other == x_coordinate && (y_other == y_coordinate-1 )) 
                || (x_other == x_coordinate && (y_other == y_coordinate+1 ))
                || (y_other == y_coordinate && x_other == x_coordinate-1)
                || (y_other == y_coordinate && x_other == x_coordinate+1);
    }
}
