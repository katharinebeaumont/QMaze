/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.Environment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
@Setter
@Getter
@RequiredArgsConstructor
public class Room {
    
    private boolean open = true;
    private double reward = 0;
    private final int x;
    private final int y;
    
    public Room(Coordinates coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }
    public boolean hasSameLocation(Room otherRoom) {
        return otherRoom.getX() == x && otherRoom.getY() == y;
    }
    
    public boolean adjoins(Room otherRoom) {
        int x_other = otherRoom.getX();
        int y_other = otherRoom.getY();
        return (x_other == x && (y_other == y -1 ))
                || (x_other == x && (y_other == y +1 ))
                || (y_other == y && x_other == x -1)
                || (y_other == y && x_other == x +1);
    }
}
