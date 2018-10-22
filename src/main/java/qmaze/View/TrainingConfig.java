package qmaze.View;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author katharine
 */
@Getter
@ToString
@AllArgsConstructor
public class TrainingConfig {
    private final int episodes;
    private final int rows;
    private final int columns;
    private final double gamma;
    private final double epsilon;
    private final double alpha;
}
