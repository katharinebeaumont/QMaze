package qmaze.Environment;

/**
 *
 * @author katharine
 * COLUMN IS X
 * ROW IS Y
 */
public class Coordinates {
    int x_coordinate;
    int y_coordinate;
    
    public Coordinates(int x_coordinate, int y_coordinate) {
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
    }
    
    
    public Coordinates(Coordinates coordinates) {
        this.x_coordinate = coordinates.getXCoordinate();
        this.y_coordinate = coordinates.getYCoordinate();
    }
    
    public int getXCoordinate() {
        return x_coordinate;
    }
    
    public int getYCoordinate() {
        return y_coordinate;
    }
    
    @Override
    public String toString() {
        return "Y(row): " + y_coordinate + ", X(col): " + x_coordinate;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!Coordinates.class.isAssignableFrom(other.getClass())) {
            return false;
        }
        Coordinates otherCoordinates = (Coordinates)other;
        return (x_coordinate == otherCoordinates.getXCoordinate() && y_coordinate == otherCoordinates.getYCoordinate());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x_coordinate;
        hash = 97 * hash + this.y_coordinate;
        return hash;
    }
}
