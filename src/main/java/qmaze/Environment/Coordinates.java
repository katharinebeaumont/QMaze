package qmaze.Environment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author katharine
 * COLUMN IS X
 * ROW IS Y
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Coordinates {
    int x;
    int y;
    
    public Coordinates(Coordinates coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }
}
