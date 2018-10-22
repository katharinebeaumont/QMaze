package qmaze.Agent;

import qmaze.Environment.Coordinates;

/**
 * @author katharine
 */
public class NoWhereToGoException extends Exception {
    NoWhereToGoException(Coordinates state) {
        super("I have no-where to go from here: " + state.toString());
    }
}
