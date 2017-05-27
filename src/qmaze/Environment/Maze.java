package qmaze.Environment;

import java.util.ArrayList;

/**
 *TODO: consistently do x,y or coordinates
 * @author katharine
 * The Maze knows:
 *  - It's size
 *  - The rooms it contains
 *  - Where the start and end (goal) is
 * The Maze does not know:
 *  - About the agent
 * Through the Maze we can ask:
 *  - Questions about state for specific locations in the maze.
 * Through the Maze we can control:
 *  - Locations that are opened or closed
 * Shouldn't do:
 *  - Reveal rooms.
 * 
 * COLUMN IS X
 * ROW IS Y
 */
public class Maze {
    
    private final ArrayList<Room> rooms;
    private Room goal;
    
    public Maze(int rows, int columns) {
        this.rooms = new ArrayList();
        buildMaze(rows, columns);
    }
    
    private Room getRoom(Coordinates state) {
        
        Room roomToFind = new Room(state);
        for (Room room: rooms) {
            if (room.hasSameLocation(roomToFind)) {
                return room;
            }
        }
        return null;
    }
    
    public void setOpen(Coordinates state, boolean open) {
        
        Room r = getRoom(state);
        r.open(open);
    }
    
    public void setGoalState(Coordinates state, int reward) {
        goal = getRoom(state);
        goal.setReward(reward);
    }
    
    public boolean isGoalState(Coordinates state) {
        Room room = getRoom(state);
        return room.equals(goal);
    }
    
    public double getReward(Coordinates action) {
        Room r = getRoom(action);
        return r.getReward();
    }
    
    private void buildMaze(int rows, int columns) {
        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {
                Room r = new Room(column, row);
                rooms.add(r);
            }
        }
    }
    
    public ArrayList<Coordinates> getAdjoiningStates(Coordinates state) {
        Room r = getRoom(state);
        ArrayList<Coordinates> adjoiningRooms = new ArrayList();
        rooms.stream().filter((otherRoom) -> (otherRoom.isOpen() && otherRoom.adjoins(r))).forEachOrdered((otherRoom) -> {
            adjoiningRooms.add(new Coordinates(otherRoom.getXCoordinate(), otherRoom.getYCoordinate()));
        });
        return adjoiningRooms;
   }   
}
