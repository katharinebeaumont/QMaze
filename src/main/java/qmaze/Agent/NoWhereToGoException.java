package qmaze.Agent;

import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class NoWhereToGoException extends Exception {
    
    public NoWhereToGoException(Coordinates state) {
        super("I have no-where to go from here: " + state.toString());
    }
    
        
    public NoWhereToGoException(String message) {
        super(message);
    }
    
}
